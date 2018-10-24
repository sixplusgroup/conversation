package finley.gmair.service.impl;

import finley.gmair.dao.OutPm25HourlyDao;
import finley.gmair.model.machine.OutPm25Hourly;
import finley.gmair.service.OutPm25HourlyService;
import finley.gmair.util.ResponseCode;
import finley.gmair.util.ResultData;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Map;

@Service
public class OutPm25HourlyServiceImpl implements OutPm25HourlyService {
    @Autowired
    private OutPm25HourlyDao outPm25HourlyDao;

    @Override
    public ResultData create(OutPm25Hourly outPm25Hourly){
        ResultData result = new ResultData();
        ResultData response = outPm25HourlyDao.insert(outPm25Hourly);
        if (response.getResponseCode() == ResponseCode.RESPONSE_ERROR) {
            result.setResponseCode(ResponseCode.RESPONSE_ERROR);
            result.setDescription(new StringBuffer("Fail to insert out pm2.5 hourly into database.").toString());
            return result;
        }
        result.setData(response.getData());
        return result;
    }

    @Override
    public ResultData fetch(Map<String, Object> condition){
        ResultData result = new ResultData();
        ResultData response = outPm25HourlyDao.query(condition);
        if (response.getResponseCode() == ResponseCode.RESPONSE_ERROR) {
            result.setResponseCode(ResponseCode.RESPONSE_ERROR);
            result.setDescription("Fail to fetch out pm2.5 hourly from database.");
            return result;
        }
        else if (response.getResponseCode() == ResponseCode.RESPONSE_NULL) {
            result.setResponseCode(ResponseCode.RESPONSE_NULL);
            result.setDescription("No out pm2.5 houry found.");
            return result;
        }
        result.setData(response.getData());
        result.setDescription("success to find out pm2.5 hourly");
        return result;
    }

    @Override
    public ResultData updateByMachineId(Map<String, Object> condition){
        ResultData result = new ResultData();
        ResultData response = outPm25HourlyDao.updateByMachineId(condition);
        if(response.getResponseCode()==ResponseCode.RESPONSE_ERROR){
            result.setResponseCode(ResponseCode.RESPONSE_ERROR);
            result.setDescription("Fail to update out pm2.5 hourly by modelId");
            return result;
        }
        result.setResponseCode(ResponseCode.RESPONSE_OK);
        result.setDescription("success to update out pm2.5 hourly by modelId");
        return result;
    }
}
