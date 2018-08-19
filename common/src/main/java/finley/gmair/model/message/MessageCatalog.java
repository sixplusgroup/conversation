package finley.gmair.model.message;

public enum MessageCatalog {
    REGISTRATION(0), AUTHENTICATION(1), NOTIFICATION_NOTREACHABLE(2), NOTIFICATION_DELIVERY(3), NOTIFICATION_INSTALLATION(4), NOTIFICATION_GENERAL(5);

    private static MessageCatalog[] list = new MessageCatalog[]{REGISTRATION, AUTHENTICATION, NOTIFICATION_NOTREACHABLE, NOTIFICATION_DELIVERY, NOTIFICATION_INSTALLATION, NOTIFICATION_GENERAL};

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
