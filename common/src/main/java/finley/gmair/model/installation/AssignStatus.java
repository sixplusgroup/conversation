package finley.gmair.model.installation;

import finley.gmair.model.EnumValue;

public enum AssignStatus implements EnumValue {
    TODOASSIGN(0), ASSIGNED(1), PROCESSING(2), FINISHED(3);

    private int value;

    AssignStatus(int value) {
        this.value = value;
    }

    @Override
    public int getValue() {
        return value;
    }
}
