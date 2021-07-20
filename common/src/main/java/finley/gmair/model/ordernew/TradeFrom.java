package finley.gmair.model.ordernew;

import finley.gmair.model.EnumValue;

/**
 * @author ：tsl
 * @date ：Created in 2020/10/22 11:24
 * @description ：订单来源，天猫，微信小程序，支付宝小程序
 */

public enum TradeFrom implements EnumValue {
    TMALL(0, "天猫"), WECHAT(1, "微信小程序"), ALIPAY(2, "支付宝小程序"),FREE(3,"免费创建");

    private int value;

    private String desc;

    TradeFrom(int value, String desc) {
        this.value = value;
        this.desc = desc;
    }

    @Override
    public int getValue() {
        return value;
    }

    public String getDesc() {return desc;}
}
