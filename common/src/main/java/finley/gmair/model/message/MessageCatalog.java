package finley.gmair.model.message;

public enum MessageCatalog {
    REGISTRATION(0), AUTHENTICATION(1), NOTIFICATION_DISPATCHED(2), NOTIFICATION_INSTALL(3);

    private static MessageCatalog[] list = new MessageCatalog[]{REGISTRATION, AUTHENTICATION, NOTIFICATION_DISPATCHED, NOTIFICATION_INSTALL};

    private int code;

    MessageCatalog(int code) {
        this.code = code;
    }

    public int getCode() {
        return this.code;
    }

    public static MessageCatalog fromValue(int value) {
        if (value < 0 || value > list.length) {
            return null;
        }
        return list[value];
    }
}
