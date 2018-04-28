package finley.gmair.model.installation;

import finley.gmair.model.EnumValue;

public enum ReconnaissanceStatus implements EnumValue {
    TODO(0), UNREACHABLE(1), FINISHED(2);

    private int value;

    ReconnaissanceStatus(int value) {
        this.value = value;
    }

    @Override
    public int getValue() {
        return 0;
    }
}
