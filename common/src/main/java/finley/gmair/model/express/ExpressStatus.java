package finley.gmair.model.express;

import finley.gmair.model.EnumValue;

public enum ExpressStatus implements EnumValue {
    ASSIGNED(0), PICKED(1), SHIPPING(2), RECEIVED(3), RETURNED(4);

    private static ExpressStatus[] list = new ExpressStatus[]{ASSIGNED, PICKED, SHIPPING, RECEIVED, RETURNED};

    int value;

    ExpressStatus(int value) {
        this.value = value;
    }

    public static ExpressStatus fromValue(int value) {
        if (value < 0 || value > list.length) {
            return null;
        }
        return list[value];
    }

    public int getValue() {
        return value;
    }
}
