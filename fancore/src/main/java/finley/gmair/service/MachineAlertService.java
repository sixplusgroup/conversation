package finley.gmair.service;

import finley.gmair.model.mqtt.MachineAlert;
import finley.gmair.util.ResultData;

import java.util.Map;

public interface MachineAlertService {
    ResultData fetch(Map<String, Object> condition);
    ResultData create(MachineAlert alert);
    ResultData modifySingle(Map<String, Object> condition);
    ResultData modifyBatch(Map<String, Object> condition);
}
