package finley.gmair.service;

import finley.gmair.model.mqtt.LoggerRecord;
import finley.gmair.util.ResultData;

import java.util.Map;


public interface LoggerRecordService {

    ResultData create(LoggerRecord loggerRecord);

    ResultData getById(Map<String, Object> condition);

    ResultData list(Map<String, Object> condition);

    ResultData count(Map<String, Object> condition);
}
