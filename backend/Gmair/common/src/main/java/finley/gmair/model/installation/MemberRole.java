package finley.gmair.model.installation;

import finley.gmair.model.EnumValue;

public enum MemberRole implements EnumValue {
    ORDINARY(0), LEADER(1);

    private int value;

    private static MemberRole[] roles = new MemberRole[]{ORDINARY, LEADER};

    MemberRole(int value) {
        this.value = value;
    }

    @Override
    public int getValue() {
        return value;
    }

    public static MemberRole fromValue(int value) {
        if (value > roles.length - 1 || value < 0) return null;
        return roles[value];
    }
}
