package finley.gmair.model.wechat;

public enum DescriptionType {
    TEXT(0), MACHINE_LIST(1);

    private int code;

    DescriptionType(int code) {
        this.code = code;
    }

    public static DescriptionType convertToDescriptionType(int code) {
        DescriptionType descriptionType = DescriptionType.TEXT;
        switch (code) {
            case 0: descriptionType = DescriptionType.TEXT; break;
            case 1: descriptionType = DescriptionType.MACHINE_LIST; break;
            default: break;
        }
        return descriptionType;
    }

    public int getCode() {
        return code;
    }
}
