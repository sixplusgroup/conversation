package finley.gmair.model.minivoice;

import finley.gmair.model.EnumValue;

/**
 * 用户通过哪个平台进行操作
 */
public enum PlatformType implements EnumValue {

    COMMON(0, "通用平台"),

    VOICE(1, "语音平台"),

    TMALL_GENIE(2, "天猫精灵"),

    XIAO_AI(3, "小爱同学");

    private int value;

    private String name;

    PlatformType(int value, String name) {
        this.value = value;
        this.name = name;
    }

    public String getName() {
        return this.name;
    }

    @Override
    public int getValue() {
        return this.value;
    }
}
