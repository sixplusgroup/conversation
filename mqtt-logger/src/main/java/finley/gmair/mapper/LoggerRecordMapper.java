package finley.gmair.mapper;

import finley.gmair.model.mqtt.LoggerRecord;

/**
 * @author zjd123
 * @date 2020/1/8 - 16:13
 */
public interface LoggerRecordMapper {
    void insert(LoggerRecord loggerRecord);
}
