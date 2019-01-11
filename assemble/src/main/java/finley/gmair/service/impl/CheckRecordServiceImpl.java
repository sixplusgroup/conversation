package finley.gmair.service.impl;

import finley.gmair.dao.CheckRecordDao;
import finley.gmair.model.assemble.CheckRecord;
import finley.gmair.service.CheckRecordService;
import finley.gmair.util.ResponseCode;
import finley.gmair.util.ResultData;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Map;

@Service
public class CheckRecordServiceImpl implements CheckRecordService {
    @Autowired
    private CheckRecordDao checkRecordDao;

    @Override
    public ResultData create(CheckRecord checkRecord){
        ResultData result = new ResultData();
        ResultData response = checkRecordDao.insert(checkRecord);
        if (response.getResponseCode() == ResponseCode.RESPONSE_ERROR) {
            result.setResponseCode(ResponseCode.RESPONSE_ERROR);
            result.setDescription(new StringBuffer("Fail to insert check record into database.").toString());
            return result;
        }
        result.setData(response.getData());
        return result;
    }

    @Override
    public ResultData fetch(Map<String, Object> condition){
        ResultData result = new ResultData();
        ResultData response = checkRecordDao.query(condition);
        if (response.getResponseCode() == ResponseCode.RESPONSE_ERROR) {
            result.setResponseCode(ResponseCode.RESPONSE_ERROR);
            result.setDescription("Fail to fetch check record from database.");
            return result;
        }
        else if (response.getResponseCode() == ResponseCode.RESPONSE_NULL) {
            result.setResponseCode(ResponseCode.RESPONSE_NULL);
            result.setDescription("No check record found.");
            return result;
        }
        result.setData(response.getData());
        result.setDescription("success to find check record");
        return result;
    }

}
