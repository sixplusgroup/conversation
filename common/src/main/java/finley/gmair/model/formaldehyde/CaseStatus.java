package finley.gmair.model.formaldehyde;

import finley.gmair.model.EnumValue;

public enum CaseStatus implements EnumValue {
    //已提交、已审核、已撤销、已隐藏
    COMMITED(0), CHECKED(1), REVOKE(2), HIDE(3);

    private int value;

    CaseStatus(int value) {
        this.value = value;
    }

    @Override
    public int getValue() {
        return value;
    }
}
