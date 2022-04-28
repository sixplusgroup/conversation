package finley.gmair.model.drift;

import finley.gmair.model.EnumValue;

public enum DriftOrderStatus implements EnumValue {
    APPLIED(0), PAYED(1), CONFIRMED(2), DELIVERED(3), BACK(4), FINISHED(5), CLOSED(6), CANCELED(7);

    private int value;

    DriftOrderStatus(int value) {
        this.value = value;
    }

    @Override
    public int getValue() {
        return value;
    }
}
