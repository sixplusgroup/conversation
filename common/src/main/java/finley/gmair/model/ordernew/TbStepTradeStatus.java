package finley.gmair.model.ordernew;

import finley.gmair.model.EnumValue;

/**
 * @author zm
 * @date 2020/10/21 0021 12:04
 * @description 分阶段付款的订单状态：（例如万人团订单等），目前有三返回状态。
 **/
public enum  TbStepTradeStatus implements EnumValue {
    /*定金未付尾款未付*/
    FRONT_NOPAID_FINAL_NOPAID(0),

    /*定金已付尾款未付*/
    FRONT_PAID_FINAL_NOPAID(1),

    /*定金和尾款都付*/
    FRONT_PAID_FINAL_PAID(2);

    private int value;

    TbStepTradeStatus(int value) {
        this.value = value;
    }

    @Override
    public int getValue() {
        return this.value;
    }
}
