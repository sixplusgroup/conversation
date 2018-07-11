package finley.gmair.service;

import finley.gmair.model.machine.MachineDefaultLocation;
import finley.gmair.util.ResultData;

import java.util.Map;

public interface MachineDefaultLocationService {
    ResultData create(MachineDefaultLocation machineDefaultLocation);

    ResultData fetch(Map<String, Object> condition);

    ResultData modify(Map<String, Object> condition);

}
