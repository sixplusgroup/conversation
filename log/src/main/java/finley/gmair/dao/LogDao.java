package finley.gmair.dao;

import finley.gmair.model.log.MachineComLog;
import finley.gmair.util.ResultData;

public interface LogDao {
    ResultData insertMachineComLog(MachineComLog machineComLog);

    ResultData queryMachineComLog(String uid);

    ResultData queryAllMachineComLog();
}
