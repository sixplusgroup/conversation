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
     * 根据传入的父订单Trade向crm中新增订单
     *
     * @author zm
     * @param trade      
     * @return finley.gmair.util.ResultData        
     * @date 2020/10/26 0026 10:42
     **/
    ResultData createTrade(Trade trade);

    /**
     * 在createTrade方法中添加单个子订单order方法
     *
     * @author zm
     * @param order
     * @return finley.gmair.util.ResultData        
     * @date 2020/10/26 0026 10:42
     **/
    ResultData createOrder(Order order);
}
