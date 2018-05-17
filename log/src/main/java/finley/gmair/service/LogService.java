package finley.gmair.service;

import finley.gmair.model.log.MachineComLog;
import finley.gmair.util.ResultData;

import java.util.Map;


public interface LogService {
    ResultData createMachineComLog(MachineComLog machineComLog);

    ResultData fetchMachineComLog(Map<String, Object> condition);

}
