package finley.gmair.dao.impl;

import finley.gmair.dao.LoggerRecordDao;
import finley.gmair.model.mqtt.LoggerRecord;
import finley.gmair.util.IDGenerator;
import finley.gmair.util.ResponseCode;
import finley.gmair.util.ResultData;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Map;

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

    @Override
    public ResultData selectOne(Map<String, Object> condition) {
        ResultData resultData = new ResultData();
        try{
            LoggerRecord loggerRecord = sqlSession.selectOne("gmair.mqtt.logger_record.selectOne", condition);
            resultData.setData(loggerRecord);
        }catch (Exception e){
            resultData.setResponseCode(ResponseCode.RESPONSE_ERROR);
            resultData.setDescription(e.getMessage());
        }
        return resultData;
    }

    @Override
    public ResultData list(Map<String, Object> condition) {
        ResultData resultData = new ResultData();
        try{
            List<LoggerRecord> recordList = sqlSession.selectList("gmair.mqtt.logger_record.list", condition);
            resultData.setData(recordList);
        }catch(Exception e){
            resultData.setResponseCode(ResponseCode.RESPONSE_ERROR);
            resultData.setDescription(e.getMessage());
        }
        return resultData;
    }

    @Override
    public ResultData count(Map<String, Object> condition) {
        ResultData resultData = new ResultData();
        try{
            Long count = sqlSession.selectOne("gmair.mqtt.logger_record.count", condition);
            resultData.setData(count);
        }catch(Exception e){
            resultData.setResponseCode(ResponseCode.RESPONSE_ERROR);
            resultData.setDescription(e.getMessage());
        }
        return resultData;
    }


}
