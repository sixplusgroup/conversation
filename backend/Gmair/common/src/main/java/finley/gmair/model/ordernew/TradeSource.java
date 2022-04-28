package finley.gmair.model.ordernew;

/**
 * @author zm
 * @date 2020/10/26 0026 17:55
 * @description 中台 -> crm系统转存时候的交易渠道来源
 **/
public enum TradeSource {
    /*天猫*/
    TMALL("58");

    private String value;

    TradeSource(String value) {
        this.value = value;
    }

    public String getValue() {
        return this.value;
    }
}
