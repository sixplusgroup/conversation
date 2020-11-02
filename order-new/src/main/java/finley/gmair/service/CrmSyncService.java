package finley.gmair.service;

import finley.gmair.model.ordernew.Trade;
import finley.gmair.util.ResultData;

/**
 * @author zm
 * @date 2020/10/26 0026 10:38
 * @description Crm系统订单同步Service
 **/
public interface CrmSyncService {


    /**
     * @author zm
     * @param [interTrade] finley.gmair.model.ordernew.Trade
     * @date 2020/11/02 14:46
     * @description 将中台系统中的交易状态变化同步到CRM系统
     **/
    ResultData updateOrderStatus(Trade interTrade);

    /**
     * @author zm
     * @param [interTrade] finley.gmair.model.ordernew.Trade
     * @date 2020/11/02 14:46
     * @description 将中台系统中的新增交易同步到CRM系统
     **/
    ResultData createNewOrder(Trade interTrade);
}