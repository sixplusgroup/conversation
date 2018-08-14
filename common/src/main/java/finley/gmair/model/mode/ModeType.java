package finley.gmair.model.mode;

import finley.gmair.model.EnumValue;

public enum ModeType implements EnumValue {
    POWER_SAVING(0), NORMAL(1), HIGH(2);

    private int value;

    ModeType(int value) {
        this.value = value;
    }

    @Override
    public int getValue() {
        return value;
    }
}
