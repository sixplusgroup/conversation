package finley.gmair.model.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * @author ：tsl
 * @date ：Created in 2021-12-17 17:48
 * @description ：子订单状态枚举
 */

@Getter
@AllArgsConstructor
public enum OrderStatusEnum {

    /*状态未知或解析失败*/
    UNKNOWN(0, "未知状态"),

    /*等待买家付款*/
    WAIT_BUYER_PAY(1, "等待买家付款"),

    /*交易取消（未付款）*/
    TRADE_CANCELED(2, "交易取消（未付款）"),

    /*等待卖家发货*/
    WAIT_SELLER_SEND_GOODS(3, "等待卖家发货"),

    /*等待买家确认收货*/
    WAIT_BUYER_CONFIRM_GOODS(4, "等待买家确认收货"),

    /*交易成功*/
    TRADE_FINISHED(5, "交易成功"),

    /*交易关闭（退款成功）*/
    TRADE_CLOSED(6, "交易关闭（退款成功）"),

    /*交易锁定(京东特有)*/
    TRADE_LOCKED(7, "交易锁定");

    private int value;

    private String desc;

    public static OrderStatusEnum getEnumByValue(Integer value) {
        for (OrderStatusEnum e : OrderStatusEnum.values()) {
            if (e.value == value) {
                return e;
            }
        }
        return UNKNOWN;
    }

    public boolean isPushCrm() {
        return WAIT_SELLER_SEND_GOODS.equals(this) ||
                WAIT_BUYER_CONFIRM_GOODS.equals(this) ||
                TRADE_FINISHED.equals(this);
    }

    public boolean isPushDrift() {
        return WAIT_SELLER_SEND_GOODS.equals(this) ||
                WAIT_BUYER_CONFIRM_GOODS.equals(this) ||
                TRADE_FINISHED.equals(this);
    }

}
