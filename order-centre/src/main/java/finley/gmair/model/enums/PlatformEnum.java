package finley.gmair.model.enums;

import finley.gmair.model.EnumValue;
import finley.gmair.model.ordernew.TradeFrom;
import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * @author ：tsl
 * @date ：Created in 2021-12-17 17:39
 * @description ：主订单平台枚举
 */

@Getter
@AllArgsConstructor
public enum PlatformEnum implements EnumValue {
    UNKNOWN(0, "未知"),
    TMALL(1, "天猫"),
    JD(2, "京东");

    private int value;

    private String desc;

    public static PlatformEnum getEnumByValue(Integer value) {
        for (PlatformEnum e : PlatformEnum.values()) {
            if (e.value == value) {
                return e;
            }
        }
        return UNKNOWN;
    }

    public TradeFrom toTradeFrom() {
        if (this == TMALL) {
            return TradeFrom.TMALL;
        } else if (this == JD) {
            return TradeFrom.JD;
        }
        return null;
    }
}
