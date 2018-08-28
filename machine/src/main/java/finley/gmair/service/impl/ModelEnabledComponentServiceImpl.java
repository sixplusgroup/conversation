package finley.gmair.service.impl;

import finley.gmair.dao.ModelEnabledComponentDao;
import finley.gmair.model.machine.ModelEnabledComponent;
import finley.gmair.service.ModelEnabledComponentService;
import finley.gmair.util.ResponseCode;
import finley.gmair.util.ResultData;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Map;

@Service
public class ModelEnabledComponentServiceImpl implements ModelEnabledComponentService {
    @Autowired
    private ModelEnabledComponentDao modelEnabledComponentDao;

    @Override
    public ResultData create(ModelEnabledComponent modelEnabledComponent){
        ResultData result = new ResultData();
        ResultData response = modelEnabledComponentDao.insert(modelEnabledComponent);
        if (response.getResponseCode() == ResponseCode.RESPONSE_ERROR) {
            result.setResponseCode(ResponseCode.RESPONSE_ERROR);
            result.setDescription(new StringBuffer("Fail to insert model enabled component into database.").toString());
            return result;
        }
        result.setData(response.getData());
        return result;
    }

    @Override
    public ResultData fetch(Map<String, Object> condition){
        ResultData result = new ResultData();
        ResultData response = modelEnabledComponentDao.query(condition);
        if (response.getResponseCode() == ResponseCode.RESPONSE_ERROR) {
            result.setResponseCode(ResponseCode.RESPONSE_ERROR);
            result.setDescription("Fail to fetch model enabled component.");
            return result;
        }
        else if (response.getResponseCode() == ResponseCode.RESPONSE_NULL) {
            result.setResponseCode(ResponseCode.RESPONSE_NULL);
            result.setDescription("No model enabled component found.");
            return result;
        }
        result.setData(response.getData());
        result.setDescription("success to find model enabled component");
        return result;
    }

}
