package finley.gmair.model.machine.v2;

import finley.gmair.model.Entity;

public class MachineSetting extends Entity {
    private String settingId;

    private String consumerId;

    private String settingName;

    private String codeValue;

    public MachineSetting() {
        super();
    }

    public MachineSetting(String consumerId, String settingName, String codeValue) {
        this();
        this.consumerId = consumerId;
        this.settingName = settingName;
        this.codeValue = codeValue;
    }

    public MachineSetting(String settingName) {
        this.settingName = settingName;
    }

    public String getSettingId() {
        return settingId;
    }

    public String getConsumerId() {
        return consumerId;
    }

    public void setConsumerId(String consumerId) {
        this.consumerId = consumerId;
    }

    public void setSettingId(String settingId) {
        this.settingId = settingId;
    }

    public String getSettingName() {
        return settingName;
    }

    public void setSettingName(String settingName) {
        this.settingName = settingName;
    }

    public String getCodeValue() {
        return codeValue;
    }

    public void setCodeValue(String codeValue) {
        this.codeValue = codeValue;
    }
}
