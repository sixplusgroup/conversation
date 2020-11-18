package finley.gmair.model.machine;

import finley.gmair.model.EnumValue;

/**
 * @author: Bright Chan
 * @date: 2020/7/25 15:31
 * @description: EfficientFilterStatus
 */
public enum EfficientFilterStatus implements EnumValue {

    NO_NEED(0), NEED(1), URGENT_NEED(2);

    private int value;

    EfficientFilterStatus(int value) {
        this.value = value;
    }

    @Override
    public int getValue() {
        return value;
    }
}
