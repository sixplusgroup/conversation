package finley.gmair.model.bill;

import finley.gmair.model.EnumValue;

public enum SnapshotStatus implements EnumValue {

    UNPAYED(0), PAYED(1);

    private int value;

    SnapshotStatus(int value) {this.value = value;}

    @Override
    public int getValue() {
        return value;
    }
}
