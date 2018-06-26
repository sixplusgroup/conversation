package finley.gmair.model.machine;

import finley.gmair.model.EnumValue;

public enum Ownership implements EnumValue {
    OWNER(0), SHARE(1);

    private int value;

    Ownership(int value) {
        this.value = value;
    }

    @Override
    public int getValue() {
        return value;
    }
}
