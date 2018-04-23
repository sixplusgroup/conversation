package finley.gmair.model.express;

import finley.gmair.model.EnumValue;

public enum ParcelType implements EnumValue {
    MACHINE(0), ORDINARY(1);

    private static ParcelType[] list = new ParcelType[]{MACHINE, ORDINARY};

    private int value;

    ParcelType(int value) {
        this.value = value;
    }

    public static ParcelType fromValue(int value) {
        if (value < 0 || value > list.length) {
            return null;
        }
        return list[value];
    }

    @Override
    public int getValue() {
        return value;
    }
}
