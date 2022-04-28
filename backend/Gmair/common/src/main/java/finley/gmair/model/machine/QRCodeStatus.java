package finley.gmair.model.machine;

import finley.gmair.model.EnumValue;

public enum QRCodeStatus implements EnumValue {
    CREATED(0), PRE_BINDED(1), ASSIGNED(2), OCCUPIED(3), RECALLED(4);

    private int value;

    QRCodeStatus(int value) {
        this.value = value;
    }

    @Override
    public int getValue() {
        return value;
    }
}
