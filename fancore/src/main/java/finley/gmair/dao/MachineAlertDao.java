package finley.gmair.dao;

import finley.gmair.model.mqtt.MachineAlert;
import finley.gmair.util.ResultData;

import java.util.Map;

public interface MachineAlertDao {
    ResultData query(Map<String, Object> condition);
    ResultData insert(MachineAlert alert);
    ResultData updateSingle(Map<String, Object> condition);
    ResultData updateBatch(Map<String, Object> condition);
}
