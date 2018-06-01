package finley.gmair.vo.machine;

import finley.gmair.model.machine.v2.PowerAction;

public class PowerSettingVo {
    private String settingId;
    private String consumerId;
    private String settingName;
    private String codeValue;
    private PowerAction powerAction;
    private int triggerHour;
    private int triggerMinute;

    public String getSettingId() {
        return settingId;
    }

    public void setSettingId(String settingId) {
        this.settingId = settingId;
    }

    public String getConsumerId() {
        return consumerId;
    }

    public void setConsumerId(String consumerId) {
        this.consumerId = consumerId;
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

    public PowerAction getPowerAction() {
        return powerAction;
    }

    public void setPowerAction(PowerAction powerAction) {
        this.powerAction = powerAction;
    }

    public int getTriggerHour() {
        return triggerHour;
    }

    public void setTriggerHour(int triggerHour) {
        this.triggerHour = triggerHour;
    }

    public int getTriggerMinute() {
        return triggerMinute;
    }

    public void setTriggerMinute(int triggerMinute) {
        this.triggerMinute = triggerMinute;
    }
}
