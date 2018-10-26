package finley.gmair.service.impl;

import finley.gmair.dao.BoundaryPM2_5Dao;
import finley.gmair.dao.OutPm25DailyDao;
import finley.gmair.model.machine.BoundaryPM2_5;
import finley.gmair.model.machine.OutPm25Daily;
import finley.gmair.service.BoundaryPM2_5Service;
import finley.gmair.service.OutPm25DailyService;
import finley.gmair.util.ResponseCode;
import finley.gmair.util.ResultData;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Map;

@Service
public class OutPm25DailyServiceImpl implements OutPm25DailyService {
    @Autowired
    private OutPm25DailyDao outPm25DailyDao;

    @Override
    public ResultData create(OutPm25Daily outPm25Daily){
        ResultData result = new ResultData();
        ResultData response = outPm25DailyDao.insert(outPm25Daily);
        if (response.getResponseCode() == ResponseCode.RESPONSE_ERROR) {
            result.setResponseCode(ResponseCode.RESPONSE_ERROR);
            result.setDescription(new StringBuffer("Fail to insert out pm25 daily into database.").toString());
            return result;
        }
        result.setData(response.getData());
        return result;
    }

    @Override
    public ResultData fetch(Map<String, Object> condition){
        ResultData result = new ResultData();
        ResultData response = outPm25DailyDao.query(condition);
        if (response.getResponseCode() == ResponseCode.RESPONSE_ERROR) {
            result.setResponseCode(ResponseCode.RESPONSE_ERROR);
            result.setDescription("Fail to fetch from database.");
            return result;
        }
        else if (response.getResponseCode() == ResponseCode.RESPONSE_NULL) {
            result.setResponseCode(ResponseCode.RESPONSE_NULL);
            result.setDescription("No machine found should be light.");
            return result;
        }
        result.setData(response.getData());
        result.setDescription("success to find machine should be light.");
        return result;
    }

}

