package finley.gmair.model.installation;

import finley.gmair.model.EnumValue;

public enum MemberRole implements EnumValue {
    ORDINARY(0), LEADER(1);

    private int value;

    MemberRole(int value) {
        this.value = value;
    }

    @Override
    public int getValue() {
        return value;
    }
}
