package finley.gmair.service;

import finley.gmair.model.machine.MachineStatus;
import finley.gmair.util.ResultData;

public interface MachineStatusCacheService {

    ResultData generate(MachineStatus machineStatus);

    ResultData fetch(String uid);

}
