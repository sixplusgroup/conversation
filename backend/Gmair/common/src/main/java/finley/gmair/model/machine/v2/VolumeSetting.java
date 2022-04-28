package finley.gmair.model.machine.v2;

import finley.gmair.model.Entity;

/**
 * @author shenghaohu@hshvip.com
 * @date 2018/6/14
 */
public class VolumeSetting extends Entity {
    private String settingId;

    private int floorPm2_5;

    private int upperPm2_5;

    private int speedValue;

    public VolumeSetting() {
        super();
    }

    public VolumeSetting(String settingId, int floorPm2_5, int upperPm2_5, int speedValue) {
        this();
        this.settingId = settingId;
        this.floorPm2_5 = floorPm2_5;
        this.upperPm2_5 = upperPm2_5;
        this.speedValue = speedValue;
    }

    public String getSettingId() {
        return settingId;
    }

    public void setSettingId(String settingId) {
        this.settingId = settingId;
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