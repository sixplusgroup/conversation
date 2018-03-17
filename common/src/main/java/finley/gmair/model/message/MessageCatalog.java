package finley.gmair.model.message;

public enum MessageCatalog {
    REGISTRATION(0), AUTHENTICATION(1), NOTIFICATION_NOTREACHABLE(2), NOTIFICATION_DELIVERY(3), NOTIFICATION_INSTALLATION(4);

    private int code;

    MessageCatalog(int code) {
        this.code = code;
    }

    public int getCode() {
        return this.code;
    }
}
