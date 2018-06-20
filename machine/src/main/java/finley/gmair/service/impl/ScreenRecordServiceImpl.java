package finley.gmair.service.impl;

import finley.gmair.dao.ScreenRecordDao;
import finley.gmair.model.machine.v2.ScreenRecord;
import finley.gmair.service.ScreenRecordService;
import finley.gmair.util.ResponseCode;
import finley.gmair.util.ResultData;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Map;

/**
 * @author shenghaohu@hshvip.com
 * @date 2018/6/19
 */
@Service
public class ScreenRecordServiceImpl implements ScreenRecordService {

    @Autowired
    private ScreenRecordDao screenRecordDao;

    @Override
    public ResultData fetchScreenRecord(Map<String, Object> condition) {
        ResultData result = new ResultData();
        ResultData response = screenRecordDao.selectScreenRecord(condition);
        if (response.getResponseCode() == ResponseCode.RESPONSE_NULL) {
            result.setResponseCode(ResponseCode.RESPONSE_NULL);
            result.setDescription("No screen record found from database");
        }
        if (response.getResponseCode() == ResponseCode.RESPONSE_ERROR) {
            result.setResponseCode(ResponseCode.RESPONSE_ERROR);
            result.setDescription("Fail to retrieve the screen record");
        }
        if (response.getResponseCode() == ResponseCode.RESPONSE_OK) {
            result.setResponseCode(ResponseCode.RESPONSE_OK);
            result.setData(response.getData());
        }
        return result;
    }

    @Override
    public ResultData createScreenRecord(ScreenRecord record) {
        ResultData result = new ResultData();
        ResultData response = screenRecordDao.insertScreenRecord(record);
        if (response.getResponseCode() == ResponseCode.RESPONSE_OK) {
            result.setResponseCode(ResponseCode.RESPONSE_OK);
            result.setData(response.getData());
            return result;
        }
        result.setResponseCode(ResponseCode.RESPONSE_ERROR);
        result.setDescription("Fail to store screen record to database");
        return result;
    }
}