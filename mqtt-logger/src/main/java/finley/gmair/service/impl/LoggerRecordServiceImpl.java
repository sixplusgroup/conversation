package finley.gmair.service.impl;

import finley.gmair.dao.LoggerRecordDao;
import finley.gmair.model.mqtt.LoggerRecord;
import finley.gmair.service.LoggerRecordService;
import finley.gmair.util.ResponseCode;
import finley.gmair.util.ResultData;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;


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
}
