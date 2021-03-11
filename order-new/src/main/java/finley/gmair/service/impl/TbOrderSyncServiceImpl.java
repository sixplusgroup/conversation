package finley.gmair.service.impl;

import com.alibaba.fastjson.JSON;
import com.taobao.api.domain.Trade;
import com.taobao.api.request.TradeFullinfoGetRequest;
import com.taobao.api.request.TradesSoldGetRequest;
import com.taobao.api.request.TradesSoldIncrementGetRequest;
import com.taobao.api.response.TradeFullinfoGetResponse;
import com.taobao.api.response.TradesSoldGetResponse;
import com.taobao.api.response.TradesSoldIncrementGetResponse;
import finley.gmair.dao.TbUserMapper;
import finley.gmair.model.ordernew.TbUser;
import finley.gmair.service.TbOrderService;
import finley.gmair.service.TbOrderSyncService;
import finley.gmair.util.ResponseCode;
import finley.gmair.util.ResultData;
import lombok.AllArgsConstructor;
import lombok.Data;
import org.apache.commons.lang.time.DateUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;

import java.util.Date;
import java.util.List;

/**
 * @author ：tsl
 * @date ：Created in 2020/10/17 15:38
 * @description ：
 */

@Service
public class TbOrderSyncServiceImpl implements TbOrderSyncService {
    private static final String IMPORT_FIELDS = "orders,tid,num_iid,num,status,type,shipping_type,trade_from,step_trade_status," +
            "buyer_rate,created,modified,pay_time,consign_time,end_time,receiver_name,receiver_state,receiver_address,receiver_zip," +
            "receiver_mobile,receiver_phone,receiver_country,receiver_city,receiver_district,receiver_town,tmall_delivery,cn_service," +
            "delivery_cps,cutoff_minutes,delivery_time,collect_time,dispatch_time,sign_time,es_time,price,total_fee,payment,adjust_fee," +
            "received_payment,discount_fee,post_fee,credit_card_fee,step_paid_fee,buyer_message,seller_memo";

    private static final String TID = "tid";

    private static final String TID_AND_CREATED = "tid,created";

    private static final Long PAGES_SIZE = 50L;

    private static final int RETRY_TIMES = 3;

    private static final long MAX_TIME_INTERVAL = 24 * 60 * 60 * 1000L;

    private Logger logger = LoggerFactory.getLogger(TbOrderSyncServiceImpl.class);

    @Autowired
    private TbOrderService tbOrderServiceImpl;
    @Autowired
    private TbAPIServiceImpl tbAPIServiceImpl;
    @Autowired
    private TbUserMapper tbUserMapper;


    @Override
    public ResultData fullImport() {
        ResultData resultData = new ResultData();
        List<TbUser> tbUserList = tbUserMapper.selectAll();
        if (CollectionUtils.isEmpty(tbUserList)) {
            resultData.setResponseCode(ResponseCode.RESPONSE_ERROR);
            resultData.setDescription("failed to find seller info");
            return resultData;
        }
        TbUser tbUser = tbUserList.get(0);
        logger.info("startFullImport, tbUser:{}", tbUser.toString());
        //如果全量同步已执行，则返回
        if (tbUser.getLastUpdateTime() != null) {
            resultData.setResponseCode(ResponseCode.RESPONSE_ERROR);
            resultData.setDescription("fullImport has been executed");
            logger.error("fullImport has been executed");
            return resultData;
        }
        String sessionKey = tbUser.getSessionKey();
        Date startSyncTime = tbUser.getStartSyncTime();
        Date finishSyncTime = new Date();
        Date start = startSyncTime;
        //以天为粒度请求
        while (start.before(finishSyncTime)) {
            Date nextDayOfStart = DateUtils.addDays(start, 1);
            if (nextDayOfStart.after(finishSyncTime)) {
                nextDayOfStart = finishSyncTime;
            }
            ImportResult importResult = importByCreatedByPage(start, nextDayOfStart, sessionKey);
            logger.info("fullImport, startTime:{}, endTime:{}, importResult:{}",
                    start, nextDayOfStart, JSON.toJSON(importResult));
            start = DateUtils.addDays(start, 1);
        }

        //更新卖家用户信息
        tbUser.setStartSyncTime(startSyncTime);
        tbUser.setLastUpdateTime(finishSyncTime);
        tbUserMapper.updateByPrimaryKey(tbUser);
        return resultData;
    }

    @Override
    public ResultData incrementalImport() {
        ResultData resultData = new ResultData();
        List<TbUser> tbUserList = tbUserMapper.selectAll();
        if (CollectionUtils.isEmpty(tbUserList)) {
            resultData.setResponseCode(ResponseCode.RESPONSE_ERROR);
            resultData.setDescription("failed to find seller info");
            return resultData;
        }
        TbUser tbUser = tbUserList.get(0);
        //如果全量同步还未执行,则返回
        if (tbUser.getLastUpdateTime() == null) {
            resultData.setResponseCode(ResponseCode.RESPONSE_ERROR);
            resultData.setDescription("fullImport is not executed");
            logger.error("fullImport is not executed");
            return resultData;
        }
        String sessionKey = tbUser.getSessionKey();
        Date lastUpdateTime = tbUser.getLastUpdateTime();
        Date startSyncTime = tbUser.getStartSyncTime();
        Date now = new Date();
        ImportResult importResult = importByModifiedByPage(startSyncTime, lastUpdateTime, now, sessionKey);
        logger.info("incrementalImport, startTime:{}, endTime:{}, importResult:{}",
                lastUpdateTime, now, JSON.toJSON(importResult));

        //更新卖家用户信息
        tbUser.setLastUpdateTime(now);
        tbUserMapper.updateByPrimaryKey(tbUser);
        return resultData;
    }


    @Override
    public ResultData manualImportByCreated(Date startCreated, Date endCreated) {
        ResultData resultData = new ResultData();
        List<TbUser> tbUserList = tbUserMapper.selectAll();
        if (CollectionUtils.isEmpty(tbUserList)) {
            resultData.setResponseCode(ResponseCode.RESPONSE_ERROR);
            resultData.setDescription("failed to find seller info");
            return resultData;
        }
        String sessionKey = tbUserList.get(0).getSessionKey();
        ImportResult importResult = importByCreatedByPage(startCreated, endCreated, sessionKey);
        resultData.setData(importResult);
        return resultData;
    }

    @Override
    public ResultData manualImportByModified(Date startModified, Date endModified, Date startCreated) {
        ResultData resultData = new ResultData();
        List<TbUser> tbUserList = tbUserMapper.selectAll();
        if (CollectionUtils.isEmpty(tbUserList)) {
            resultData.setResponseCode(ResponseCode.RESPONSE_ERROR);
            resultData.setDescription("failed to find seller info");
            return resultData;
        }
        String sessionKey = tbUserList.get(0).getSessionKey();
        ImportResult importResult = importByModifiedByPage(startCreated, startModified, endModified, sessionKey);
        resultData.setData(importResult);
        return resultData;
    }

    @Override
    public ResultData manualImportByTid(Long tid) {
        ResultData resultData = new ResultData();
        List<TbUser> tbUserList = tbUserMapper.selectAll();
        if (CollectionUtils.isEmpty(tbUserList)) {
            resultData.setResponseCode(ResponseCode.RESPONSE_ERROR);
            resultData.setDescription("failed to find seller info");
            return resultData;
        }
        String sessionKey = tbUserList.get(0).getSessionKey();
        Trade trade = importFullInfoByTid(tid, sessionKey);
        if (trade == null) {
            resultData.setResponseCode(ResponseCode.RESPONSE_ERROR);
            resultData.setDescription("failed to find trade from taobao");
            return resultData;
        }
        resultData = tbOrderServiceImpl.handleTrade(trade);
        return resultData;
    }

    private ImportResult importByCreatedByPage(Date startCreated, Date endCreated, String sessionKey) {
        if (endCreated.getTime() - startCreated.getTime() > MAX_TIME_INTERVAL) {
            logger.error("importByCreatedByPage fail, timeInterval exceed 24 hour, start:{}, end{}", startCreated, endCreated);
            return new ImportResult(0, 0, 0, 0, 0);
        }
        int batchRequestNum = 0, batchRequestSuccessNum = 0, tradeNum = 0, tradeInfoGetSuccessNum = 0, tradeHandleSuccessNum = 0;

        TradesSoldGetRequest request = new TradesSoldGetRequest();
        request.setUseHasNext(true);
        request.setFields(TID);
        request.setPageSize(PAGES_SIZE);
        request.setUseHasNext(true);
        request.setStartCreated(startCreated);
        request.setEndCreated(endCreated);
        //分页同步
        long pageNo = 1L;
        while (true) {
            batchRequestNum++;
            request.setPageNo(pageNo++);
            logger.info("startTime:{}, endTime:{}, pageNo:{}", request.getStartCreated(), request.getEndCreated(), request.getPageNo());
            TradesSoldGetResponse response = tbAPIServiceImpl.tradesSoldGet(request, sessionKey);
            //重试
            for (int i = 0; i < RETRY_TIMES && !response.isSuccess(); i++) {
                response = tbAPIServiceImpl.tradesSoldGet(request, sessionKey);
            }
            if (!response.isSuccess()) {
                logger.error("failed to get response, response:{}", JSON.toJSONString(response));
                //如果分页请求失败，则不继续执行后面的分页
                break;
            }
            batchRequestSuccessNum++;
            if (!CollectionUtils.isEmpty(response.getTrades())) {
                for (Trade trade : response.getTrades()) {
                    tradeNum++;
                    //获取订单详情
                    Trade fullInfoTrade = importFullInfoByTid(trade.getTid(), sessionKey);
                    if (fullInfoTrade == null) {
                        continue;
                    } else {
                        tradeInfoGetSuccessNum++;
                    }
                    ResultData resultData = tbOrderServiceImpl.handleTrade(fullInfoTrade);
                    if (resultData.getResponseCode() != ResponseCode.RESPONSE_OK) {
                        logger.error("failed to handle trade, response:{}", JSON.toJSONString(resultData));
                    } else {
                        logger.info("handle trade success, response:{}", JSON.toJSONString(resultData));
                        tradeHandleSuccessNum++;
                    }
                }
            }
            if (!response.getHasNext()) {
                break;
            }
        }
        return new ImportResult(batchRequestNum, batchRequestSuccessNum, tradeNum, tradeInfoGetSuccessNum, tradeHandleSuccessNum);
    }

    private ImportResult importByModifiedByPage(Date startSyncTime, Date startModified, Date endModified, String sessionKey) {
        if (endModified.getTime() - startModified.getTime() > MAX_TIME_INTERVAL) {
            logger.error("importByModifiedByPage fail, timeInterval exceed 24 hour, start:{}, end{}", startModified, endModified);
            return new ImportResult(0, 0, 0, 0, 0);
        }
        int batchRequestNum = 0, batchRequestSuccessNum = 0, tradeNum = 0, tradeInfoGetSuccessNum = 0, tradeHandleSuccessNum = 0;

        TradesSoldIncrementGetRequest request = new TradesSoldIncrementGetRequest();
        request.setStartModified(startModified);
        request.setEndModified(endModified);
        request.setUseHasNext(true);
        //批量同步获取订单号和创建时间
        request.setFields(TID_AND_CREATED);
        request.setPageSize(PAGES_SIZE);
        long pageNo = 1L;

        //分页同步
        while (true) {
            batchRequestNum++;
            request.setPageNo(pageNo++);
            logger.info("startTime:{}, endTime:{}, pageNo:{}", request.getStartModified(), request.getEndModified(), request.getPageNo());
            TradesSoldIncrementGetResponse response = tbAPIServiceImpl.tradesSoldIncrementGet(request, sessionKey);
            //重试
            for (int i = 0; i < RETRY_TIMES && !response.isSuccess(); i++) {
                response = tbAPIServiceImpl.tradesSoldIncrementGet(request, sessionKey);
            }
            if (!response.isSuccess()) {
                logger.error("failed to get response, response:{}", JSON.toJSONString(response));
                //如果分页请求失败，则不继续执行后面的分页
                break;
            }
            batchRequestSuccessNum++;
            if (!CollectionUtils.isEmpty(response.getTrades())) {
                for (Trade trade : response.getTrades()) {
                    //增量同步限制下单时间不早于开始同步时间
                    if (startSyncTime != null && startSyncTime.after(trade.getCreated())) {
                        continue;
                    }
                    tradeNum++;
                    //获取订单详情
                    Trade fullInfoTrade = importFullInfoByTid(trade.getTid(), sessionKey);
                    if (fullInfoTrade == null) {
                        continue;
                    } else {
                        tradeInfoGetSuccessNum++;
                    }
                    ResultData resultData = tbOrderServiceImpl.handleTrade(fullInfoTrade);
                    if (resultData.getResponseCode() != ResponseCode.RESPONSE_OK) {
                        logger.error("failed to handle trade, response:{}", JSON.toJSONString(resultData));
                    } else {
                        logger.info("handle trade success, response:{}", JSON.toJSONString(resultData));
                        tradeHandleSuccessNum++;
                    }
                }
            }
            if (!response.getHasNext()) {
                break;
            }
        }
        return new ImportResult(batchRequestNum, batchRequestSuccessNum, tradeNum, tradeInfoGetSuccessNum, tradeHandleSuccessNum);
    }

    private Trade importFullInfoByTid(Long tid, String sessionKey) {
        TradeFullinfoGetRequest request = new TradeFullinfoGetRequest();
        request.setTid(tid);
        request.setFields(IMPORT_FIELDS);
        TradeFullinfoGetResponse response = tbAPIServiceImpl.tradeFullInfoGet(request, sessionKey);
        //重试
        for (int i = 0; i < RETRY_TIMES && !response.isSuccess(); i++) {
            response = tbAPIServiceImpl.tradeFullInfoGet(request, sessionKey);
        }
        if (!response.isSuccess()) {
            logger.error("failed to get response, response:{}", JSON.toJSONString(response));
            return null;
        }
        return response.getTrade();
    }

    @Data
    @AllArgsConstructor
    private static class ImportResult {
        //批量请求数
        private int batchRequestNum;

        //批量请求成功数
        private int batchRequestSuccessNum;

        //批量请求获得订单数
        private int tradeNum;

        //订单详情请求成功数
        private int tradeInfoGetSuccessNum;

        //订单处理成功数
        private int tradeHandleSuccessNum;
    }
}
