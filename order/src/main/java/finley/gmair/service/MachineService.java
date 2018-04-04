package finley.gmair.service;

import finley.gmair.util.ResultData;

import java.util.Map;

public interface MachineService {

    ResultData fetchInstallType(Map<String, Object> condition);

    ResultData fetchSetupProvider(Map<String, Object> condition);
}
