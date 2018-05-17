package finley.gmair.dao;

import finley.gmair.model.log.MachineComLog;
import finley.gmair.util.ResultData;

import java.util.Map;

public interface LogDao {
    ResultData insertMachineComLog(MachineComLog machineComLog);

    ResultData queryMachineComLog(Map<String, Object> condition);

}
