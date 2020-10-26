package finley.gmair.service;

import finley.gmair.model.ordernew.Order;
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
     * 将中台系统中的单个order（一个子订单）转存到crm系统中
     *
     * @author zm
     * @param order
     * @return finley.gmair.util.ResultData        
     * @date 2020/10/26 0026 10:42
     **/
    ResultData createOrder(Order order);
}
