package finley.gmair.model.machine;

import finley.gmair.model.EnumValue;

public enum Ownership implements EnumValue {
    OWNER(0), SHARE(1);

    private int value;

    private static Ownership[] roles = new Ownership[]{OWNER, SHARE};

    Ownership(int value) {
        this.value = value;
    }

    @Override
    public int getValue() {
        return value;
    }

    public static Ownership fromValue(int value) {
        if (value > roles.length - 1 || value < 0) return null;
        return roles[value];
    }
}
