package finley.gmair.dao;

import finley.gmair.model.mqtt.LoggerRecord;
import finley.gmair.util.ResultData;

import java.util.Map;


public interface LoggerRecordDao {

    ResultData insert(LoggerRecord loggerRecord);

    ResultData selectOne(Map<String, Object> condition);

    ResultData list(Map<String, Object> condition);

    ResultData count(Map<String, Object> condition);

}
