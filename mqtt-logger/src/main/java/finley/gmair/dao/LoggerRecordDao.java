package finley.gmair.dao;

import finley.gmair.model.mqtt.LoggerRecord;
import finley.gmair.util.ResultData;


public interface LoggerRecordDao {

    ResultData insert(LoggerRecord loggerRecord);

    ResultData selectOne(String recordId);
}
