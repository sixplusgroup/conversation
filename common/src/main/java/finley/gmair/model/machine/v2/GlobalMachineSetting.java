package finley.gmair.model.machine.v2;

import finley.gmair.model.Entity;

/**
 * @author shenghaohu@hshvip.com
 * @date 2018/6/19
 */
public class GlobalMachineSetting extends Entity {
    private String globalMachineSettingId;

    private String globalMachineSettingName;

    public GlobalMachineSetting() {
        super();
    }

    public GlobalMachineSetting(String globalMachineSettingName) {
        this();
        this.globalMachineSettingName = globalMachineSettingName;
    }

    public String getGlobalMachineSettingId() {
        return globalMachineSettingId;
    }

    public void setGlobalMachineSettingId(String globalMachineSettingId) {
        this.globalMachineSettingId = globalMachineSettingId;
    }

    public String getGlobalMachineSettingName() {
        return globalMachineSettingName;
    }

    public void setGlobalMachineSettingName(String globalMachineSettingName) {
        this.globalMachineSettingName = globalMachineSettingName;
    }
}