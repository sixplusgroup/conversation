package finley.gmair.model.machine.v2;

import finley.gmair.model.Entity;

public class MachineSetting extends Entity {
    private String settingId;

    private String settingName;

    public MachineSetting(String settingName) {
        this.settingName = settingName;
    }

    public String getSettingId() {
        return settingId;
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
}
