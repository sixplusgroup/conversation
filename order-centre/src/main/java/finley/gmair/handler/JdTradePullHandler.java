package finley.gmair.handler;

import com.alibaba.fastjson.JSON;
import com.google.common.base.Preconditions;
import com.google.common.collect.Lists;
import com.jd.open.api.sdk.domain.order.OrderQueryJsfService.response.get.OrderSearchInfo;
import com.jd.open.api.sdk.request.order.PopOrderGetRequest;
import com.jd.open.api.sdk.request.order.PopOrderSearchRequest;
import com.jd.open.api.sdk.response.order.PopOrderGetResponse;
import com.jd.open.api.sdk.response.order.PopOrderSearchResponse;
import finley.gmair.api.ApiCallException;
import finley.gmair.api.JdApi;
import finley.gmair.converter.JdTradeConverter;
import finley.gmair.model.domain.ShopAuthorizeInfo;
import finley.gmair.model.domain.UnifiedTrade;
import finley.gmair.model.enums.PlatformEnum;
import finley.gmair.model.request.BatchPullRequest;
import finley.gmair.model.request.SinglePullRequest;
import finley.gmair.model.result.PullResult;
import finley.gmair.util.DateUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;
import java.util.List;
import java.util.stream.Collectors;

/**
 * @author ：tsl
 * @date ：Created in 2021-12-21 21:23
 * @description ：
 */

@Component
public class JdTradePullHandler extends AbstractTradePullHandler<OrderSearchInfo> implements InitializingBean {

    private Logger logger = LoggerFactory.getLogger(JdTradePullHandler.class);

    @Resource
    JdTradeConverter jdTradeConverter;

    @Override
    protected OrderSearchInfo singlePull(SinglePullRequest request, ShopAuthorizeInfo authorizeInfo, PullResult result) {
        JdApi jdApi = JdApi.setUp(authorizeInfo.getSessionKey(), authorizeInfo.getAppKey(), authorizeInfo.getAppSecret());
        PopOrderGetRequest jdRequest = new PopOrderGetRequest();
        jdRequest.setOrderId(Long.parseLong(request.getTid()));
        jdRequest.setOptionalFields(JdApi.Constant.ORDER_GET_FIELDS);
        try {
            PopOrderGetResponse response = jdApi.execute(jdRequest);
            if (response == null || response.getOrderDetailInfo() == null ||
                    response.getOrderDetailInfo().getOrderInfo() == null) {
                result.addTradeInfoGetFailNum();
                logger.error("singlePull error,failed to parse popOrderGetResponse,request:{},response:{}", JSON.toJSONString(request), JSON.toJSONString(response));
                return null;
            }
            result.addTradeInfoGetSuccessNum();
            return response.getOrderDetailInfo().getOrderInfo();
        } catch (ApiCallException e) {
            logger.error("singlePull error,apiCallException,request:{}", JSON.toJSONString(request), e);
            result.addTradeInfoGetFailNum();
            return null;
        }
    }

    @Override
    protected List<OrderSearchInfo> batchPull(BatchPullRequest request, ShopAuthorizeInfo authorizeInfo, PullResult result) {
        List<OrderSearchInfo> resultList = Lists.newArrayList();
        JdApi jdApi = JdApi.setUp(authorizeInfo.getSessionKey(), authorizeInfo.getAppKey(), authorizeInfo.getAppSecret());
        Preconditions.checkArgument(request.getEndTime().getTime()
                - request.getStartTime().getTime() <= JdApi.Constant.MAX_TIME_INTERVAL, "开始时间和结束时间间隔不能超过一天");

        int curPage = 1, pageSize = request.getPageSize();
        PopOrderSearchRequest jdRequest = new PopOrderSearchRequest();
        jdRequest.setStartDate(DateUtil.date2Str(request.getStartTime()));
        jdRequest.setEndDate(DateUtil.date2Str(request.getEndTime()));
        jdRequest.setOptionalFields(JdApi.Constant.ORDER_SEARCH_FIELDS);
        jdRequest.setOrderState(JdApi.Constant.ORDER_STATUS);
        jdRequest.setPageSize(String.valueOf(pageSize));
        jdRequest.setPage(String.valueOf(curPage));
        jdRequest.setSortType(request.isDesc() ? 1 : 0);
        jdRequest.setDateType(request.isSortByModified() ? 0 : 1);

        //todo:重试,异常处理,PullResult记录
        try {
            List<String> tidList = Lists.newArrayList();
            PopOrderSearchResponse response = jdApi.execute(jdRequest);
            if (!response.getSearchorderinfoResult().getApiResult().getSuccess()) {
                result.addTradeSearchFailNum();
                return resultList;
            }
            if (response.getSearchorderinfoResult().getOrderTotal() == 0) {
                return resultList;
            }
            List<String> subTidList = response.getSearchorderinfoResult().getOrderInfoList().stream().map(
                    com.jd.open.api.sdk.domain.order.OrderQueryJsfService.response.search.OrderSearchInfo::getOrderId)
                    .collect(Collectors.toList());
            tidList.addAll(subTidList);
            // 分页遍历
            while (curPage * pageSize < response.getSearchorderinfoResult().getOrderTotal()) {
                curPage++;
                jdRequest.setPage(String.valueOf(curPage));
                response = jdApi.execute(jdRequest);
                subTidList = response.getSearchorderinfoResult().getOrderInfoList().stream().map(
                        com.jd.open.api.sdk.domain.order.OrderQueryJsfService.response.search.OrderSearchInfo::getOrderId)
                        .collect(Collectors.toList());
                tidList.addAll(subTidList);
            }
            for (String tid : tidList) {
                PopOrderGetRequest jdRequest2 = new PopOrderGetRequest();
                jdRequest2.setOrderId(Long.parseLong(tid));
                jdRequest2.setOptionalFields(JdApi.Constant.ORDER_GET_FIELDS);
                PopOrderGetResponse response2 = jdApi.execute(jdRequest2);
                resultList.add(response2.getOrderDetailInfo().getOrderInfo());
            }
            return resultList;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    @Override
    protected UnifiedTrade convert(OrderSearchInfo originalTrade) {
        return jdTradeConverter.jdTrade2UnifiedTrade(originalTrade);
    }

    @Override
    protected String getTid(OrderSearchInfo originalTrade) {
        return originalTrade.getOrderId();
    }

    @Override
    protected PlatformEnum getPlatform() {
        return PlatformEnum.JD;
    }

    @Override
    public void afterPropertiesSet() {
        TradePullHandlerContext.registerHandler(getPlatform().getValue(), this);
    }
}
