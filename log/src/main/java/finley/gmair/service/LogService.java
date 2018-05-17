package finley.gmair.service;

import finley.gmair.model.log.MachineComLog;
import finley.gmair.util.ResultData;


public interface LogService {
    ResultData createMachineComLog(MachineComLog machineComLog);
}
