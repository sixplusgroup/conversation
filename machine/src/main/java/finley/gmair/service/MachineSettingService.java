package finley.gmair.service;

import finley.gmair.model.machine.v2.GlobalMachineSetting;
import finley.gmair.model.machine.v2.LightSetting;
import finley.gmair.model.machine.v2.MachineSetting;
import finley.gmair.model.machine.v2.VolumeSetting;
import finley.gmair.util.ResultData;

import java.util.Map;

public interface MachineSettingService {
    ResultData fetchPowerActionMachine();

    ResultData fetchMachineSetting(Map<String, Object> condition);

    ResultData createMachineSetting(MachineSetting setting);

    ResultData fetchVolumeSetting(Map<String, Object> condition);

    ResultData createVolumeSetting(VolumeSetting setting);

    ResultData modifyVolumeSetting(VolumeSetting setting);

    ResultData fetchLightSetting(Map<String, Object> condition);

    ResultData createLightSetting(LightSetting setting);

    ResultData modifyLightSetting(LightSetting setting);

    ResultData fetchGlobalMachineSetting(Map<String, Object> condition);

    ResultData createGlobalMachineSetting(GlobalMachineSetting setting);
}
