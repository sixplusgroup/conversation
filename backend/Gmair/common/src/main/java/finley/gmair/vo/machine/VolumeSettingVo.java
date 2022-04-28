package finley.gmair.vo.machine;

/**
 * @author shenghaohu@hshvip.com
 * @date 2018/6/14
 */
public class VolumeSettingVo {
    private String settingId;

    private String consumerId;

    private String settingName;

    private String codeValue;

    private int floorPm2_5;

    private int upperPm2_5;

    private int speedValue;

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

    public int getFloorPm2_5() {
        return floorPm2_5;
    }

    public void setFloorPm2_5(int floorPm2_5) {
        this.floorPm2_5 = floorPm2_5;
    }

    public int getUpperPm2_5() {
        return upperPm2_5;
    }

    public void setUpperPm2_5(int upperPm2_5) {
        this.upperPm2_5 = upperPm2_5;
    }

    public int getSpeedValue() {
        return speedValue;
    }

    public void setSpeedValue(int speedValue) {
        this.speedValue = speedValue;
    }
}