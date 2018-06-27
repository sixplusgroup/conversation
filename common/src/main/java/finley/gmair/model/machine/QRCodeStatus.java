package finley.gmair.model.machine;

public enum QRCodeStatus {
    CREATED(0), PRE_BINDED(1), ASSIGNED(2), OCCUPIED(3), RECALLED(4);

    private int value;

    QRCodeStatus(int value) {
        this.value = value;
    }

    public int getValue() {
        return value;
    }
}
