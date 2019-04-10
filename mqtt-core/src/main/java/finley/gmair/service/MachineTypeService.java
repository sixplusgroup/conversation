package finley.gmair.service;

import finley.gmair.model.mqtt.MachineType;
import finley.gmair.util.ResultData;

import java.util.Map;

public interface MachineTypeService {
    ResultData fetch(Map<String, Object> condition);

    ResultData create(MachineType machineType);
}
