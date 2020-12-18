package finley.gmair.service.strategy.impl;

import finley.gmair.model.dto.CrmOrderDTO;
import finley.gmair.model.ordernew.CrmOrderStatus;
import finley.gmair.model.ordernew.TbTradeStatus;
import finley.gmair.service.strategy.CrmStatusTransStrategy;
import org.springframework.stereotype.Service;

/**
 * @author zm
 * @date 2020/11/05 3:42 下午
 * @description CRM订单-> 淘宝订单状态的转换策略
 */
@Service
public class CrmOrderTrans implements CrmStatusTransStrategy {

    @Override
    public TbTradeStatus transCrmOrderStatus(CrmOrderDTO crmOrderDTO) {
        TbTradeStatus tbRes = null;
        int crmStatus = Integer.parseInt(crmOrderDTO.getBillstat());
        CrmOrderStatus crmOrderStatus = CrmOrderStatus.getOrderStatusEnumByValue(crmStatus);

        if(crmOrderStatus != null){
            switch (crmOrderStatus) {
                case UNTREATED:
                case PRE_SURVEY_CONTACT_FAILED:
                case UNPAID_REMOTEFEE:
                case CONSIDER_WHETHER_INSTALL:
                case WAITING_SCHEDULED:
                case SURVEYED_REQUEST_DELIVERY_DELAY:
                case SURVEYED_NO_DELIVERY:
                case CONTACTED_WAITING_NOTIFICATION:
                case GLASS_SURVEYED:
                case SCHEDULING:
                case NOT_IN_INSTALLATION_AREA:
                    // 均为等待卖家发货阶段
                    tbRes = TbTradeStatus.WAIT_SELLER_SEND_GOODS;
                    break;
                case SCHEDULE_INSTALLATION_CONTACT_FAILED:
                case DELIVERED_IN_TRANSIT:
                case DELIVERED_NO_SCHEDULED:
                case DELIVERED_OF_ACCESSORIES_BOX:
                case PRIORITY_DELIVERED_BY_ACCESSORIES_BOX:
                    // 等待买家确认收货
                    tbRes = TbTradeStatus.WAIT_BUYER_CONFIRM_GOODS;
                    break;
                case PARTIAL_INSTALLATION_COMPLETED:
                case ALL_INSTALLATION_COMPLETED:
                case READY_TO_RETURN:
                    // 已收货
                    tbRes = TbTradeStatus.TRADE_FINISHED;
                    break;
                case GOODS_RETURNED:
                    tbRes = TbTradeStatus.TRADE_CLOSED;
                    break;
                default:
                    tbRes = null;
            }
        }
        return tbRes;
    }
}
