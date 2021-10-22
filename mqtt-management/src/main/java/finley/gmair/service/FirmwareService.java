package finley.gmair.service;

import finley.gmair.model.mqttManagement.Firmware;
import finley.gmair.util.ResultData;

import java.util.Map;

public interface FirmwareService {
    ResultData create(Firmware firmware);

    ResultData fetch(Map<String, Object> condition);

    ResultData modify(Map<String, Object> condition);
}
