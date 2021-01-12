package finley.gmair.model.minivoice;

import finley.gmair.model.EnumValue;

/**
 * 通过所有平台的操作类型
 */
public enum OperateType implements EnumValue {

    WEATHER(0),

    DEVICE(1);

    private int value;

    OperateType(int value) {
        this.value = value;
    }

    @Override
    public int getValue() {
        return this.value;
    }
}
