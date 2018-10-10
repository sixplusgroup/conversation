package finley.gmair.model.drift;

import finley.gmair.model.EnumValue;

public enum EXCodeStatus implements EnumValue {
    CREATED(0), OCCUPIED(1);

    private int value;

    EXCodeStatus(int value) {this.value = value;}

    @Override
    public int getValue() {
        return value;
    }
}
