package finley.gmair.form.machine;

/**
 * @author shenghaohu@hshvip.com
 * @date 2018/6/15
 */
public class MachineSettingForm {
    private String consumerId;

    private String settingName;

    private String codeValue;

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
}