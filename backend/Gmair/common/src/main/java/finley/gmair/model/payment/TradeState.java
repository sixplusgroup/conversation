package finley.gmair.model.payment;

import finley.gmair.model.EnumValue;

public enum TradeState implements EnumValue {

    UNPAYED(0), PAYED(1), REFUNDED(2),;

    private int value;

    TradeState(int value) {this.value = value;}

    @Override
    public int getValue() {
        return value;
    }
}
