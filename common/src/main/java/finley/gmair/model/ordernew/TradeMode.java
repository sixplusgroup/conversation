package finley.gmair.model.ordernew;

import finley.gmair.model.EnumValue;

/**
 * @author zm
 * @date 2020/11/03 16:57
 * @description 中台系统交易的处理状态
 **/
public enum  TradeMode implements EnumValue {
    // 初始状态
    INITIAL(0),
    // 已经去模糊化
    DEBLUR(1),
    // 已经推送到CRM系统
    PUSHED_TO_CRM(2);

    private int value;

    TradeMode(int value) {
        this.value = value;
    }

    @Override
    public int getValue() {
        return value;
    }
}
