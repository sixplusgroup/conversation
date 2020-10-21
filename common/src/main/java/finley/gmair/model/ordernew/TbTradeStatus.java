package finley.gmair.model.ordernew;

import finley.gmair.model.EnumValue;

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
        return 0;
    }
}
