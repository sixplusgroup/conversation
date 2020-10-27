package finley.gmair.service;

import finley.gmair.model.ordernew.CrmOrderStatus;
import finley.gmair.model.ordernew.Order;
import finley.gmair.model.ordernew.TbTradeStatus;
import finley.gmair.model.ordernew.Trade;
import finley.gmair.util.ResultData;

/**
 * @author zm
 * @date 2020/10/26 0026 10:38
 * @description Crm系统订单Service
 **/
public interface CrmOrderService {
    /**
     * 将中台系统中的trade（一个父订单）转存到crm系统中
     *
     * @author zm
     * @param trade      
     * @return finley.gmair.util.ResultData        
     * @date 2020/10/26 0026 10:42
     **/
    ResultData createTrade(Trade trade);

    /**
     * @author zm
     * @param tbTradeStatus
     * @return finley.gmair.model.ordernew.CrmOrderStatus
     * @description 中台订单状态（即tb的交易状态）转换为crm的订单状态
     */
    CrmOrderStatus transTbTradeStat(TbTradeStatus tbTradeStatus);

    /**
     * @author zm
     * @param crmOrderStatus
     * @return finley.gmair.model.ordernew.TbTradeStatus
     * @description crm的订单状态转为中台订单状态（即tb的交易状态）
     */
    TbTradeStatus transCrmOrderStat(CrmOrderStatus crmOrderStatus);
}
