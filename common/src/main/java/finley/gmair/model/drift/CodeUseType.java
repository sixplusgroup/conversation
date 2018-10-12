package finley.gmair.model.drift;

import finley.gmair.model.EnumValue;

public enum CodeUseType implements EnumValue {
    PAPER(0), PRICE(1);

    private int value;

    CodeUseType(int value) { this.value = value; }

    @Override
    public int getValue() {
        return value;
    }
}
