package finley.gmair.model.wechat;

public enum DescriptionType {
    TEXT(0), MACHINE_LIST(1);

    private int code;

    DescriptionType(int code) {
        this.code = code;
    }

    public int getCode() {
        return code;
    }
}
