package finley.gmair.dao;

import finley.gmair.model.log.MachineComLog;
import finley.gmair.model.log.Server2MachineLog;
import finley.gmair.model.log.SystemEventLog;
import finley.gmair.model.log.UserActionLog;
import finley.gmair.util.ResultData;

import java.util.Map;

public interface LogDao {
    ResultData insertMachineComLog(MachineComLog machineComLog);

    ResultData queryMachineComLog(Map<String, Object> condition);

    ResultData insertModuleLog(SystemEventLog systemEventLog);

    ResultData queryModuleLog(Map<String, Object> condition);

    ResultData insertUserActionLog(UserActionLog userActionLog);

    ResultData queryUserActionLog(Map<String, Object> condition);

    ResultData insertServer2MachineLog(Server2MachineLog server2MachineLog);

    ResultData queryServer2MachineLog(Map<String, Object> condition);

}
