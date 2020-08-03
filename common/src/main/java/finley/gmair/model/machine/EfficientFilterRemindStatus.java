package finley.gmair.model.machine;

import finley.gmair.model.EnumValue;

/**
 * @author: Bright Chan
 * @date: 2020/7/25 15:45
 * @description: TODO
 */
public enum EfficientFilterRemindStatus implements EnumValue {
    REMIND_ZERO(0), REMIND_ONCE(1), REMIND_TWICE(2);

    private int value;

    EfficientFilterRemindStatus(int value) {
        this.value = value;
    }

    @Override
    public int getValue() {
        return value;
    }
}
