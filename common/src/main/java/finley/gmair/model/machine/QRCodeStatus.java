package finley.gmair.model.machine;

public enum QRCodeStatus {
    CREATED(0), ASSIGNED(1), OCCUPIED(2), RECALLED(3);

    private int value;

    QRCodeStatus(int value) {
        this.value = value;
    }

    public int getValue() {
        return value;
    }
}
