package finley.gmair.model.machine;

import finley.gmair.model.EnumValue;

/**
 * @author: Bright Chan
 * @date: 2020/11/7 16:51
 * @description: MachineFilterType
 */
public enum MachineFilterType implements EnumValue {
    /**
     * PRIMARY: 初效滤网
     * EFFICIENT: 高效滤网
     */
    PRIMARY(0), EFFICIENT(1);

    private int value;

    MachineFilterType(int value) {
        this.value = value;
    }

    @Override
    public int getValue() {
        return this.value;
    }
}
