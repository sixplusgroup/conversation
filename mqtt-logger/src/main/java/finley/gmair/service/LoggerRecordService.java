package finley.gmair.service;

import finley.gmair.model.mqtt.LoggerRecord;
import finley.gmair.util.ResultData;


public interface LoggerRecordService {

    ResultData create(LoggerRecord loggerRecord);

    ResultData getById(String recordId);
}
