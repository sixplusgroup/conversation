package finley.gmair.dao;

import finley.gmair.model.mqtt.MachineType;
import finley.gmair.util.ResultData;

import java.util.Map;

public interface MachineTypeDao {
    ResultData queryType(Map<String, Object> condition);

    ResultData insertType(MachineType machineType);
}
