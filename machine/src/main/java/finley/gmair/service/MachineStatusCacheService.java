package finley.gmair.service;

import finley.gmair.model.machine.v2.MachineLiveStatus;
import finley.gmair.util.ResultData;

public interface MachineStatusCacheService {

    ResultData generate(MachineLiveStatus machineStatus);

    ResultData fetch(String uid);

}
