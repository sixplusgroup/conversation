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
    TRADE_CLOSED_BY_TAOBAO(0, "付款前，卖家或买家主动关闭交易"),

    /*等待买家付款*/
    WAIT_BUYER_PAY(1, "等待买家付款"),

    /*等待卖家发货*/
    WAIT_SELLER_SEND_GOODS(2, "等待卖家发货"),

    /*卖家部分发货*/
    SELLER_CONSIGNED_PART(3, "卖家部分发货"),

    /*等待买家确认收货*/
    WAIT_BUYER_CONFIRM_GOODS(4, "等待买家确认收货"),

    /*交易成功（正常购物流程的最终状态）*/
    TRADE_FINISHED(5, "交易成功"),

    /*交易关闭（申请退款流程的最终状态）*/
    TRADE_CLOSED(6, "交易关闭");

    private int value;

    private String desc;

    TbTradeStatus(int value, String desc) {
        this.value = value;
        this.desc = desc;
    }

    @Override
    public int getValue() {
        return this.value;
    }

    public String getDesc() {
        return desc;
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

    /**
     * 判断是否能够推送给CRM系统（如果是被taobao关闭或者未支付 -> 返回false）
     */
    public boolean judgeCrmAdd() {
        return !(this == TRADE_CLOSED_BY_TAOBAO || this == WAIT_BUYER_PAY);
    }
}
