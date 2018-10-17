package finley.gmair.model.drift;

import finley.gmair.model.EnumValue;

public enum DriftOrderStatus implements EnumValue {
    APPLIED(0), PAYED(1), CONFIRMED(2), DELIVERED(3), FINISHED(4), CLOSED(5), CANCELED(6);

    private int value;

    DriftOrderStatus(int value) {
        this.value = value;
    }

    @Override
    public int getValue() {
        return value;
    }
}
