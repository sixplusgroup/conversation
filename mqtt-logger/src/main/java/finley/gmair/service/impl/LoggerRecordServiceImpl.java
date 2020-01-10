package finley.gmair.service.impl;

import finley.gmair.dao.LoggerRecordDao;
import finley.gmair.model.mqtt.LoggerRecord;
import finley.gmair.service.LoggerRecordService;
import finley.gmair.util.ResponseCode;
import finley.gmair.util.ResultData;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.Map;


@Service
public class LoggerRecordServiceImpl implements LoggerRecordService {

    @Resource
    private LoggerRecordDao loggerRecordDao;


    @Override
    public ResultData create(LoggerRecord loggerRecord) {
        ResultData result = new ResultData();
        ResultData response = loggerRecordDao.insert(loggerRecord);
        if (response.getResponseCode() != ResponseCode.RESPONSE_OK) {
            result.setResponseCode(ResponseCode.RESPONSE_ERROR);
            result.setDescription("Fail to insert loggerRecord to database");
            return result;
        }
        result.setResponseCode(ResponseCode.RESPONSE_OK);
        result.setData(response.getData());
        return result;
    }

    @Override
    public ResultData getById(Map<String, Object> condition) {
        ResultData result = new ResultData();
        ResultData response = loggerRecordDao.selectOne(condition);
        if(response.getResponseCode() != ResponseCode.RESPONSE_OK){
            result.setResponseCode(ResponseCode.RESPONSE_ERROR);
            result.setDescription("Fail to get result from database");
            return result;
        }
        result.setData(response.getData());
        result.setResponseCode(ResponseCode.RESPONSE_OK);
        return result;
    }

    @Override
    public ResultData list(Map<String, Object> condition) {
        ResultData result = new ResultData();
        ResultData response = loggerRecordDao.list(condition);
        if(response.getResponseCode() != ResponseCode.RESPONSE_OK){
            result.setResponseCode(ResponseCode.RESPONSE_ERROR);
            result.setDescription("fail to get result from database");
            return result;
        }
        result.setData(response.getData());
        result.setResponseCode(ResponseCode.RESPONSE_OK);
        return result;
    }

    @Override
    public ResultData count(Map<String, Object> condition) {
        ResultData result = new ResultData();
        ResultData response = loggerRecordDao.selectOne(condition);
        if(response.getResponseCode() != ResponseCode.RESPONSE_OK){
            result.setResponseCode(ResponseCode.RESPONSE_ERROR);
            result.setDescription("fail to get result from database");
            return result;
        }
        result.setData(response.getData());
        result.setResponseCode(ResponseCode.RESPONSE_OK);
        return result;
    }


}
