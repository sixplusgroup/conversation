package finley.gmair.dao;

import finley.gmair.model.log.MachineComLog;
import finley.gmair.model.log.SystemEventLog;
import finley.gmair.util.ResultData;

import java.util.Map;

public interface LogDao {
    ResultData insertMachineComLog(MachineComLog machineComLog);

    ResultData queryMachineComLog(Map<String, Object> condition);

    ResultData insertModuleLog(SystemEventLog systemEventLog);

    ResultData queryModuleLog(Map<String, Object> condition);

}
