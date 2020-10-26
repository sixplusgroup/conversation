package finley.gmair.model.ordernew;

import finley.gmair.model.EnumValue;

/**
 * @author zm
 * @date 2020/10/26 0026 17:55
 * @description 中台 -> crm系统转存时候的交易渠道来源
 **/
public enum TradeSource implements EnumValue {
    /*淘宝*/
    TAOBAO(0),
    /*58*/
    WUBA(58),
    ;

    private int value;

    TradeSource(int value) {
        this.value = value;
    }

    @Override
    public int getValue() {
        return this.value;
    }
}
