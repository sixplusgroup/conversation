package finley.gmair.model.ordernew;

import finley.gmair.model.EnumValue;

/**
 * @author zm
 * @date 2020/10/21 0021 12:01
 * @description 淘宝交易内部来源
 **/
public enum TbTradeFrom implements EnumValue {
    /*手机*/
    WAP(0),
    /*嗨淘*/
    HITAO(1),
    /*TOP平台*/
    TOP(2),
    /*普通淘宝*/
    TAOBAO(3),
    /*聚划算*/
    JHS(4);

    private int value;

    TbTradeFrom(int value) {
        this.value = value;
    }

    @Override
    public int getValue() {
        return this.value;
    }
}
