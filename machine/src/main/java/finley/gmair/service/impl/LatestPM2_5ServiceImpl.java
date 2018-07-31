package finley.gmair.service.impl;

import finley.gmair.dao.BoundaryPM2_5Dao;
import finley.gmair.dao.LatestPM2_5Dao;
import finley.gmair.model.machine.BoundaryPM2_5;
import finley.gmair.model.machine.LatestPM2_5;
import finley.gmair.service.BoundaryPM2_5Service;
import finley.gmair.service.LatestPM2_5Service;
import finley.gmair.util.ResponseCode;
import finley.gmair.util.ResultData;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Map;

@Service
public class LatestPM2_5ServiceImpl implements LatestPM2_5Service {
    @Autowired
    private LatestPM2_5Dao latestPM2_5Dao;

    @Override
    public ResultData create(LatestPM2_5 latestPM2_5){
        ResultData result = new ResultData();
        ResultData response = latestPM2_5Dao.insert(latestPM2_5);
        if (response.getResponseCode() == ResponseCode.RESPONSE_ERROR) {
            result.setResponseCode(ResponseCode.RESPONSE_ERROR);
            result.setDescription(new StringBuffer("Fail to insert latest pm2.5 into database.").toString());
            return result;
        }
        result.setData(response.getData());
        return result;
    }

    @Override
    public ResultData fetch(Map<String, Object> condition){
        ResultData result = new ResultData();
        ResultData response = latestPM2_5Dao.query(condition);
        if (response.getResponseCode() == ResponseCode.RESPONSE_ERROR) {
            result.setResponseCode(ResponseCode.RESPONSE_ERROR);
            result.setDescription("Fail to fetch latest pm2.5 from database.");
            return result;
        }
        else if (response.getResponseCode() == ResponseCode.RESPONSE_NULL) {
            result.setResponseCode(ResponseCode.RESPONSE_NULL);
            result.setDescription("No latest pm2.5 found.");
            return result;
        }
        result.setData(response.getData());
        result.setDescription("success to find pm2.5");
        return result;
    }

    @Override
    public ResultData updateByMachineId(Map<String, Object> condition){
        ResultData result = new ResultData();
        ResultData response = latestPM2_5Dao.updateByMachineId(condition);
        if(response.getResponseCode()==ResponseCode.RESPONSE_ERROR){
            result.setResponseCode(ResponseCode.RESPONSE_ERROR);
            result.setDescription("Fail to update latest pm2.5 by modelId");
            return result;
        }
        result.setResponseCode(ResponseCode.RESPONSE_OK);
        result.setDescription("success to update latest pm2.5 by modelId");
        return result;
    }
}
