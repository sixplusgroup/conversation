package finley.gmair.service;

import com.alibaba.fastjson.JSON;
import com.google.common.collect.ImmutableMap;
import finley.gmair.converter.DriftOrderConverter;
import finley.gmair.model.domain.UnifiedTrade;
import finley.gmair.model.drift.DriftOrderExpress;
import finley.gmair.model.enums.PushStatusEnum;
import finley.gmair.repo.UnifiedTradeRepo;
import finley.gmair.util.ResponseCode;
import finley.gmair.util.ResultData;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;

import javax.annotation.Resource;
import java.util.Map;

/**
 * @author ：tsl
 * @date ：Created in 2021-12-24 16:39
 * @description ：
 */

@Service
public class DriftPushService {

    private Logger logger = LoggerFactory.getLogger(DriftPushService.class);

    private static final Map<String, String> ID_NAME_MAP =
            ImmutableMap.of("57146510696", "甲醛检测试纸", "70463602876", "GM甲醛检测仪");
    @Resource
    DriftOrderConverter converter;
    @Resource
    DriftClient driftClient;
    @Resource
    UnifiedTradeRepo unifiedTradeRepo;

    public ResultData pushCreate(String tradeId) {
        logger.info("try to pushCreate to drift,tradeId:{}", tradeId);
        ResultData result = new ResultData();
        UnifiedTrade trade = unifiedTradeRepo.findById(tradeId);
        if (null == trade) {
            result.setResponseCode(ResponseCode.RESPONSE_ERROR);
            result.setDescription("failed to find unifiedTrade by tradeId");
            logger.error("failed to find unifiedTrade by tradeId,tradeId:{}", tradeId);
            return result;
        }

        ResultData checkResult = check(trade);
        if (checkResult.getResponseCode() == ResponseCode.RESPONSE_ERROR) {
            logger.info("trade check failed,reason:{}", checkResult.getDescription());
            return checkResult;
        }

        DriftOrderExpress driftOrderExpress = converter.unifiedTrade2Drift(trade);
        ResultData syncResult = driftClient.syncCreate(driftOrderExpress);
        if (syncResult.getResponseCode() != ResponseCode.RESPONSE_OK) {
            logger.error("pushCreate to drift error,request{},response:{}", JSON.toJSONString(driftOrderExpress), JSON.toJSONString(syncResult));
            trade.setDriftPushStatus(PushStatusEnum.PUSHED_FAIL.getValue());
            result.setResponseCode(ResponseCode.RESPONSE_ERROR);
            result.setDescription("同步创建Drift失败");
        } else {
            logger.info("pushCreate to drift success,request:{}", JSON.toJSONString(driftOrderExpress));
            trade.setDriftPushStatus(PushStatusEnum.PUSHED.getValue());
            result.setResponseCode(ResponseCode.RESPONSE_OK);
        }
        unifiedTradeRepo.updateTradeOnly(trade);
        return result;
    }

    /**
     * Drift同步前置条件:
     * 1、有甲醛检测租赁(试纸)商品项目
     * 2、未同步过Drift
     * 3、已去模糊化
     *
     * @param trade
     * @return
     */
    public ResultData check(UnifiedTrade trade) {
        ResultData result = new ResultData();
        if (null == trade) {
            result.setDescription("trade is null");
            result.setResponseCode(ResponseCode.RESPONSE_ERROR);
        } else if (trade.getIsFuzzy()) {
            result.setDescription("trade is fuzzy");
            result.setResponseCode(ResponseCode.RESPONSE_ERROR);
        } else if (trade.getDriftPushStatus() != PushStatusEnum.NOT_PUSH.getValue()) {
            result.setDescription("trade is pushed");
            result.setResponseCode(ResponseCode.RESPONSE_ERROR);
        } else if (CollectionUtils.isEmpty(converter.filterDriftOrder(trade))) {
            result.setDescription("trade orderList filtered is empty");
            result.setResponseCode(ResponseCode.RESPONSE_ERROR);
        }
        return result;
    }
}
