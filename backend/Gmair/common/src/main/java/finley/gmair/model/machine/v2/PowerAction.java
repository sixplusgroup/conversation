package finley.gmair.model.machine.v2;

import finley.gmair.model.EnumValue;

public enum PowerAction implements EnumValue {
    TURN_ON(0), TURN_OFF(1);

    private int value;

    PowerAction(int value) {
        this.value = value;
    }

    @Override
    public int getValue() {
        return this.value;
    }
}
