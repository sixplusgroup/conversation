package finley.gmair.model.order;

import finley.gmair.model.EnumValue;

public enum OrderStatus implements EnumValue {
    PAYED(0), PROCESSING(1), FINISHED(2), COMMENTED(3), CLOSED(4);

    private int value;

    OrderStatus(int value) {
        this.value = value;
    }

    @Override
    public int getValue() {
        return value;
    }
}
