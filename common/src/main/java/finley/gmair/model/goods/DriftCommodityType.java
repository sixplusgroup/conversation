package finley.gmair.model.goods;

import finley.gmair.model.EnumValue;

public enum DriftCommodityType implements EnumValue{
    PAPER(0), DETECTOR(1);

    private int value;

    DriftCommodityType(int value) {
        this.value = value;
    }

    @Override
    public int getValue() {
        return value;
    }
}
