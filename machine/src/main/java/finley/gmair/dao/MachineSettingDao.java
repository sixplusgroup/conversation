package finley.gmair.dao;

import finley.gmair.model.machine.v2.LightSetting;
import finley.gmair.model.machine.v2.MachineSetting;
import finley.gmair.model.machine.v2.VolumeSetting;
import finley.gmair.util.ResultData;

import java.util.Map;

public interface MachineSettingDao {
    ResultData selectPowerSetting(Map<String, Object> condition);

    ResultData queryMachineSetting(Map<String, Object> condition);

    ResultData insertMachineSetting(MachineSetting setting);

    ResultData selectVolumeSetting(Map<String, Object> condition);

    ResultData insertVolumeSetting(VolumeSetting setting);

    ResultData updateVolumeSetting(VolumeSetting setting);

    ResultData selectLightSetting(Map<String, Object> condition);

    ResultData insertLightSetting(LightSetting setting);

    ResultData updateLightSetting(LightSetting setting);
}
