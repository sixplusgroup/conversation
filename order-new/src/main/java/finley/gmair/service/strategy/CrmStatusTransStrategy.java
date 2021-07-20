package finley.gmair.service.strategy;

import finley.gmair.model.dto.CrmOrderDTO;
import finley.gmair.model.ordernew.TbTradeStatus;

/**
 * @author zm
 * @date 2020/11/05 2:59 下午
 * @description CRM订单状态转换的策略接口
 */
public interface CrmStatusTransStrategy {

    /**
     *
     * @param crmOrderDTO crm订单DTO
     * @return TbTradeStatus 淘宝订单状态类
     */
    TbTradeStatus transCrmOrderStatus(CrmOrderDTO crmOrderDTO);
}
