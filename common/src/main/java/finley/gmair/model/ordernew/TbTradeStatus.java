package finley.gmair.model.ordernew;

import finley.gmair.model.EnumValue;
import finley.gmair.model.drift.DriftOrderStatus;

/**
 * @author zm
 * @date 2020/10/21 0021 9:29
 * @description 淘宝方订单状态
 **/
public enum TbTradeStatus implements EnumValue {

    /*付款前，卖家或买家主动关闭交易*/
    TRADE_CLOSED_BY_TAOBAO(0),

    /*等待买家付款*/
    WAIT_BUYER_PAY(1),

    /*等待卖家发货*/
    WAIT_SELLER_SEND_GOODS(2),

    /*卖家部分发货*/
    SELLER_CONSIGNED_PART(3),

    /*等待买家确认收货*/
    WAIT_BUYER_CONFIRM_GOODS(4),

    /*交易成功（正常购物流程的最终状态）*/
    TRADE_FINISHED(5),

    /*交易关闭（申请退款流程的最终状态）*/
    TRADE_CLOSED(6);

    private int value;

    TbTradeStatus(int value) {
        this.value = value;
    }

    @Override
    public int getValue() {
        return this.value;
    }

    public DriftOrderStatus toDriftOrderStatus() {
        switch (this) {
            case TRADE_CLOSED_BY_TAOBAO:
                return null;
            case WAIT_BUYER_PAY:
                return DriftOrderStatus.APPLIED;
            case WAIT_SELLER_SEND_GOODS:
                return DriftOrderStatus.CONFIRMED;
            case SELLER_CONSIGNED_PART:
                return DriftOrderStatus.CONFIRMED;
            case WAIT_BUYER_CONFIRM_GOODS:
                return DriftOrderStatus.DELIVERED;
            case TRADE_FINISHED:
                return DriftOrderStatus.FINISHED;
            case TRADE_CLOSED:
                return DriftOrderStatus.CANCELED;
            default:
                return null;
        }
    }

    /**
     * 淘宝订单状态转换为Crm订单状态
     */
    public CrmOrderStatus toCrmOrderStatus() {
        CrmOrderStatus crmRes;
        switch (this) {
            case TRADE_CLOSED_BY_TAOBAO:
            case WAIT_BUYER_PAY:
            case WAIT_SELLER_SEND_GOODS:
                // 未处理的初始状态
                crmRes = CrmOrderStatus.UNTREATED;
                break;
            case SELLER_CONSIGNED_PART:
            case WAIT_BUYER_CONFIRM_GOODS:
                // 部分发货、等待买家确认收货 -> 已发货运输中
                crmRes = CrmOrderStatus.DELIVERED_IN_TRANSIT;
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
