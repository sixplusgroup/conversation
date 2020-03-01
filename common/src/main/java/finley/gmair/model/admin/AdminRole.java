package finley.gmair.model.admin;

import finley.gmair.model.EnumValue;

public enum AdminRole implements EnumValue {
    MANAGER(0), INSTALL(1), CHECK(2), CUSTOM_SERVICE(3), MESSAGE(4);

    private int value;

    AdminRole(int value){this.value = value;}

    @Override
    public int getValue() {
        return value;
    }
}
