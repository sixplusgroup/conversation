package finley.gmair.dao;

import finley.gmair.model.mqtt.Firmware;
import finley.gmair.util.ResultData;

import java.util.Map;

public interface FirmwareDao {
    ResultData insert(Firmware firmware);

    ResultData query(Map<String, Object> condition);

    ResultData update(Map<String, Object> condition);
}
