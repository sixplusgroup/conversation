package finley.gmair.dao.impl;

import finley.gmair.dao.LoggerRecordDao;
import finley.gmair.model.mqtt.Firmware;
import finley.gmair.model.mqtt.LoggerRecord;
import finley.gmair.util.IDGenerator;
import finley.gmair.util.ResponseCode;
import finley.gmair.util.ResultData;
import org.springframework.stereotype.Repository;

@Repository
public class LoggerRecordDaoImpl extends BaseDao implements LoggerRecordDao {


    @Override
    public ResultData insert(LoggerRecord loggerRecord) {
        ResultData result = new ResultData();
        loggerRecord.setRecordId(IDGenerator.generate(""));
        try {
            sqlSession.insert("gmair.mqtt.logger_record.insert", loggerRecord);
            result.setData(loggerRecord);
        } catch (Exception e) {
            result.setResponseCode(ResponseCode.RESPONSE_ERROR);
            result.setDescription(e.getMessage());
        }
        return result;
    }
}
