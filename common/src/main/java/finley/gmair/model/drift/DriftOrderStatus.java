package finley.gmair.model.drift;

import finley.gmair.model.EnumValue;

public enum DriftOrderStatus implements EnumValue {
    APPLIED(0), PROCESSED(1), DELIVERED(2), FINISHED(3), CLOSED(4), CANCELED(5);

    private int value;

    DriftOrderStatus(int value) {
        this.value = value;
    }

    @Override
    public int getValue() {
        return value;
    }
}
