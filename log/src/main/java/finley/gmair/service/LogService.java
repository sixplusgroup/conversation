package finley.gmair.service;

import finley.gmair.model.log.MachineComLog;
import finley.gmair.model.log.Server2MachineLog;
import finley.gmair.model.log.SystemEventLog;
import finley.gmair.model.log.UserActionLog;
import finley.gmair.util.ResultData;

import java.util.Map;


public interface LogService {
    ResultData createMachineComLog(MachineComLog machineComLog);

    ResultData fetchMachineComLog(Map<String, Object> condition);

    ResultData createModuleLog(SystemEventLog systemEventLog);

    ResultData fetchModuleLog(Map<String, Object> condition);

    ResultData createUserActionLog(UserActionLog userActionLog);

    ResultData fetchUserActionLog(Map<String, Object> condition);

    ResultData createServer2MachineLog(Server2MachineLog server2MachineLog);

    ResultData fetchServer2MachineLog(Map<String, Object> condition);

}
