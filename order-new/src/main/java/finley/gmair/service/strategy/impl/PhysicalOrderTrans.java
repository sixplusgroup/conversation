package finley.gmair.service.strategy.impl;

import finley.gmair.model.ordernew.CrmOrderStatus;
import finley.gmair.model.ordernew.Order;
import finley.gmair.model.ordernew.TbTradeStatus;
import finley.gmair.service.strategy.TbStatusTransStrategy;
import org.springframework.stereotype.Service;

/**
 * @author zm
 * @date 2020/11/05 2:02 下午
 * @description 实物订单的订单状态转换策略
 */
@Service
public class PhysicalOrderTrans implements TbStatusTransStrategy {

    @Override
    public CrmOrderStatus transTbOrderStatus(Order interOrder) {
        CrmOrderStatus crmRes;
        String orderStatus = interOrder.getStatus();
        TbTradeStatus tbOrderStatus = TbTradeStatus.valueOf(orderStatus);

        // 订单的快递单号
        String invoiceNo =interOrder.getInvoiceNo();

        switch (tbOrderStatus) {
            case WAIT_SELLER_SEND_GOODS:
                // 未处理的初始状态
                crmRes = CrmOrderStatus.UNTREATED;
                break;
            case SELLER_CONSIGNED_PART:
            case WAIT_BUYER_CONFIRM_GOODS:
                if(invoiceNo == null || invoiceNo.equals("")){
                    // 实物订单的订单号为空的 ->已勘测，客户要求延迟发货
                    crmRes = CrmOrderStatus.SURVEYED_REQUEST_DELIVERY_DELAY;
                }else{
                    // 部分发货、等待买家确认收货 -> 已发货运输中
                    crmRes = CrmOrderStatus.DELIVERED_IN_TRANSIT;
                }
                break;
            case TRADE_CLOSED:
                // 交易关闭（退款流程的最终状态）
                crmRes = CrmOrderStatus.GOODS_RETURNED;
                break;
            case TRADE_FINISHED:
                // 交易完成（正常购物流程的最终状态）
                crmRes = CrmOrderStatus.ALL_INSTALLATION_COMPLETED;
                break;
            default:
                crmRes = null;
        }
        return crmRes;
    }
}
