package finley.gmair.model.ordernew;

import finley.gmair.model.EnumValue;

/**
 * @author ：tsl
 * @date ：Created in 2020/10/22 11:24
 * @description ：订单来源，天猫，微信小程序，支付宝小程序
 */

public enum TradeFrom implements EnumValue {
    TMALL(0), WECHAT(1), ALIPAY(2);

    private int value;

    TradeFrom(int value) {
        this.value = value;
    }

    @Override
    public int getValue() {
        return value;
    }
}
