package finley.gmair.service.strategy;

import finley.gmair.model.ordernew.CrmOrderStatus;
import finley.gmair.model.ordernew.Order;

/**
 * @author zm
 * @date 2020/11/05 2:00 下午
 * @description 淘宝订单状态转换的策略接口
 */
public interface TbStatusTransStrategy {

    /**
     *
     * @param interOrder 中台中存储的订单类
     * @return CrmOrderStatus CRM订单状态类
     */
    CrmOrderStatus transTbOrderStatus(Order interOrder);
}
