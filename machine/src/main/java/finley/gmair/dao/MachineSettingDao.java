package finley.gmair.dao;

import finley.gmair.model.machine.v2.VolumeSetting;
import finley.gmair.util.ResultData;

import java.util.Map;

public interface MachineSettingDao {
    ResultData selectPowerSetting(Map<String, Object> condition);

    ResultData queryMachineSetting(Map<String, Object> condition);

    ResultData selectVolumeSetting(Map<String, Object> condition);

    ResultData insertVolumeSetting(VolumeSetting setting);

    ResultData updateVolumeSetting(VolumeSetting setting);
}
