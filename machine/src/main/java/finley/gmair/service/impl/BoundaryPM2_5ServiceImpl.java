package finley.gmair.service.impl;

import finley.gmair.dao.BoundaryPM2_5Dao;
import finley.gmair.model.machine.BoundaryPM2_5;
import finley.gmair.service.BoundaryPM2_5Service;
import finley.gmair.util.ResponseCode;
import finley.gmair.util.ResultData;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Map;

@Service
public class BoundaryPM2_5ServiceImpl implements BoundaryPM2_5Service {
    @Autowired
    private BoundaryPM2_5Dao boundaryPM2_5Dao;

    @Override
    public ResultData create(BoundaryPM2_5 boundaryPM2_5){
        ResultData result = new ResultData();
        ResultData response = boundaryPM2_5Dao.insert(boundaryPM2_5);
        if (response.getResponseCode() == ResponseCode.RESPONSE_ERROR) {
            result.setResponseCode(ResponseCode.RESPONSE_ERROR);
            result.setDescription(new StringBuffer("Fail to insert board version into database.").toString());
            return result;
        }
        result.setData(response.getData());
        return result;
    }

    @Override
    public ResultData fetch(Map<String, Object> condition){
        ResultData result = new ResultData();
        ResultData response = boundaryPM2_5Dao.query(condition);
        if (response.getResponseCode() == ResponseCode.RESPONSE_ERROR) {
            result.setResponseCode(ResponseCode.RESPONSE_ERROR);
            result.setDescription("Fail to fetch boundary pm2.5 from database.");
            return result;
        }
        else if (response.getResponseCode() == ResponseCode.RESPONSE_NULL) {
            result.setResponseCode(ResponseCode.RESPONSE_NULL);
            result.setDescription("No boundary pm2.5 found.");
            return result;
        }
        result.setData(response.getData());
        result.setDescription("success to find pm2.5");
        return result;
    }

    @Override
    public ResultData updateByModelId(Map<String, Object> condition){
        ResultData result = new ResultData();
        ResultData response = boundaryPM2_5Dao.updateByModelId(condition);
        if(response.getResponseCode()==ResponseCode.RESPONSE_ERROR){
            result.setResponseCode(ResponseCode.RESPONSE_ERROR);
            result.setDescription("Fail to update boundary pm2.5 by modelId");
            return result;
        }
        result.setResponseCode(ResponseCode.RESPONSE_OK);
        result.setDescription("success to update boundary pm2.5 by modelId");
        return result;
    }
}
