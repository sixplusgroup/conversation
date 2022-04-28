package finley.gmair.model.machine.v2;

import finley.gmair.model.Entity;

/**
 * @author shenghaohu@hshvip.com
 * @date 2018/6/15
 */
public class LightSetting extends Entity{
    private String settingId;

    private int lightValue;

    public LightSetting() {
        super();
    }

    public LightSetting(String settingId, int lightValue) {
        this();
        this.settingId = settingId;
        this.lightValue = lightValue;
    }

    public String getSettingId() {
        return settingId;
    }

    public void setSettingId(String settingId) {
        this.settingId = settingId;
    }

    public int getLightValue() {
        return lightValue;
    }

    public void setLightValue(int lightValue) {
        this.lightValue = lightValue;
    }
}