package finley.gmair.service;

import com.alibaba.fastjson.JSONObject;
import finley.gmair.converter.CrmOrderConverter;
import finley.gmair.model.domain.UnifiedTrade;
import finley.gmair.model.enums.PushStatusEnum;
import finley.gmair.repo.UnifiedTradeRepo;
import finley.gmair.util.ResponseCode;
import finley.gmair.util.ResultData;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;

import javax.annotation.Resource;
import java.util.List;
import java.util.Objects;

/**
 * @author ：tsl
 * @date ：Created in 2021-12-24 21:56
 * @description ：
 */

@Service
public class CrmPushService {

    private Logger logger = LoggerFactory.getLogger(CrmPushService.class);

    @Resource
    CrmOrderConverter converter;
    @Resource
    CrmClient crmClient;
    @Resource
    UnifiedTradeRepo unifiedTradeRepo;

    public ResultData pushCreate(String tradeId) {
        logger.info("try to pushCreate to crm,tradeId:{}", tradeId);
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

        List<CrmOrderConverter.CrmOrder> crmOrderList = converter.unifiedTrade2Crm(trade);
        boolean isSuccess = true;
        for (CrmOrderConverter.CrmOrder crmOrder : crmOrderList) {
            JSONObject syncResult = crmClient.syncCreate(JSONObject.toJSONString(crmOrder));
            if (Objects.equals(syncResult.get("ResponseCode").toString(),
                    ResponseCode.RESPONSE_ERROR.toString())) {
                logger.error("pushCreate to crm error,param:{},response:{}", JSONObject.toJSONString(crmOrder), syncResult);
                isSuccess = false;
            } else {
                logger.info("pushCreate to crm success,param:{}", JSONObject.toJSONString(crmOrder));
            }
        }
        trade.setCrmPushStatus(isSuccess ? PushStatusEnum.PUSHED.getValue() : PushStatusEnum.PUSHED_FAIL.getValue());
        if (!isSuccess) {
            result.setResponseCode(ResponseCode.RESPONSE_ERROR);
            result.setDescription("同步创建Crm失败");
        }
        unifiedTradeRepo.updateTradeOnly(trade);
        return result;
    }

    /**
     * Crm同步前置条件:
     * 1、未同步过Crm
     * 2、已去模糊化
     * 3、存在子订单状态符合同步条件的订单
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
        } else if (trade.getCrmPushStatus() != PushStatusEnum.NOT_PUSH.getValue()) {
            result.setDescription("trade is pushed");
            result.setResponseCode(ResponseCode.RESPONSE_ERROR);
        } else if (CollectionUtils.isEmpty(converter.filterCrmOrder(trade))) {
            result.setDescription("trade orderList filtered is empty");
            result.setResponseCode(ResponseCode.RESPONSE_ERROR);
        }
        return result;
    }
}
