package finley.gmair.service;

import finley.gmair.model.ordernew.Trade;
import finley.gmair.util.ResultData;

/**
 * @author zm
 * @date 2020/10/26 0026 10:38
 * @description Crm系统订单同步Service
 **/
public interface CrmSyncService {


    ResultData updateOrderStatus(Trade interTrade);

    ResultData createNewOrder(Trade interTrade);
}