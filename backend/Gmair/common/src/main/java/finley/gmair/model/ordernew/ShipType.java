package finley.gmair.model.ordernew;

import finley.gmair.model.EnumValue;

/**
 * @author zm
 * @date 2020/10/21 0021 11:43
 * @description 创建交易时的物流方式（交易完成前物流方式可能变，但是trade中的这个字段是不变的）
 **/
public enum ShipType implements EnumValue {
    /*卖家包邮*/
    FREE(0),

    /*平邮*/
    POST(1),

    /*快递*/
    EXPRESS(2),

    /*虚拟发货*/
    VIRTUAL(3),

    /*次日必定达*/
    ARRIVED_NEXT_DAY(25),

    /*预约配送*/
    APPOINTMENT_DELIVERY(26);

    private int value;

    ShipType(int value) {
        this.value = value;
    }

    @Override
    public int getValue() {
        return this.value;
    }
}
