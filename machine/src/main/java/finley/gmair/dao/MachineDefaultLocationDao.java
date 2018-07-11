package finley.gmair.dao;

import finley.gmair.model.machine.MachineDefaultLocation;
import finley.gmair.util.ResultData;

import java.util.Map;


public interface MachineDefaultLocationDao {
    ResultData insert(MachineDefaultLocation machineDefaultLocation);

    ResultData query(Map<String, Object> condition);

    ResultData update(Map<String, Object> condition);
}
