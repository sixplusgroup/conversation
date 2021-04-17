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
    TRADE_CLOSED(6, "付款以后用户退款成功，交易自动关闭"),

    TRADE_BUYER_SIGNED(7, "买家已签收，货到付款专用"),

    TRADE_NO_CREATE_PAY(8, "没有创建支付宝交易"),

    WAIT_PRE_AUTH_CONFIRM(9, "余额宝0元购合约中"),

    PAY_PENDING(10, "国际信用卡支付付款确认中"),

    PAID_FORBID_CONSIGN(11, "该状态代表订单已付款但是处于禁止发货状态");

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
            case TRADE_NO_CREATE_PAY:
            case PAY_PENDING:
            case WAIT_PRE_AUTH_CONFIRM:
                return DriftOrderStatus.APPLIED;
            case WAIT_SELLER_SEND_GOODS:
            case PAID_FORBID_CONSIGN:
                return DriftOrderStatus.PAYED;
            case SELLER_CONSIGNED_PART:
                return DriftOrderStatus.CONFIRMED;
            case WAIT_BUYER_CONFIRM_GOODS:
                return DriftOrderStatus.DELIVERED;
            case TRADE_FINISHED:
            case TRADE_BUYER_SIGNED:
                return DriftOrderStatus.FINISHED;
            case TRADE_CLOSED:
                return DriftOrderStatus.CANCELED;
            default:
                return null;
        }
    }

    /**
     * 判断是否能够推送给CRM系统（原则是：如果买家已经付款就需要推送给CRM系统）
     */
    public boolean judgeCrmAdd() {
        return !(this == TRADE_CLOSED_BY_TAOBAO || this == WAIT_BUYER_PAY ||
                this == TRADE_NO_CREATE_PAY || this == PAY_PENDING);
    }
}
