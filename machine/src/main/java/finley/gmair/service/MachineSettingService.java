package finley.gmair.service;

import finley.gmair.model.machine.v2.VolumeSetting;
import finley.gmair.util.ResultData;

import java.util.Map;

public interface MachineSettingService {
    ResultData fetchPowerActionMachine();

    ResultData fetchMachineSetting(Map<String, Object> condition);

    ResultData fetchVolumeSetting(Map<String, Object> condition);

    ResultData createVolumeSetting(VolumeSetting setting);

    ResultData modifyVolumeSetting(VolumeSetting setting);
}
