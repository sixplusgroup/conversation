package finley.gmair.service.impl;

import finley.gmair.dao.ModelEfficientConfigDao;
import finley.gmair.model.machine.ModelEfficientConfig;
import finley.gmair.service.ModelEfficientConfigService;
import finley.gmair.util.ResponseCode;
import finley.gmair.util.ResultData;
import finley.gmair.vo.machine.FilterUpdByMQTTConfig;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;

@Service
public class ModelEfficientConfigServiceImpl implements ModelEfficientConfigService {
    @Autowired
    private ModelEfficientConfigDao modelEfficientConfigDao;

    @Override
    public ResultData create(ModelEfficientConfig modelEfficientConfig){
        ResultData result = new ResultData();
        ResultData response = modelEfficientConfigDao.insert(modelEfficientConfig);
        if (response.getResponseCode() == ResponseCode.RESPONSE_ERROR) {
            result.setResponseCode(ResponseCode.RESPONSE_ERROR);
            result.setDescription(new StringBuffer("Fail to insert model efficient config into database.").toString());
            return result;
        }
        result.setData(response.getData());
        return result;
    }

    @Override
    public ResultData fetch(Map<String, Object> condition){
        ResultData result = new ResultData();
        ResultData response = modelEfficientConfigDao.query(condition);
        if (response.getResponseCode() == ResponseCode.RESPONSE_ERROR) {
            result.setResponseCode(ResponseCode.RESPONSE_ERROR);
            result.setDescription("Fail to fetch model efficient config.");
            return result;
        }
        else if (response.getResponseCode() == ResponseCode.RESPONSE_NULL) {
            result.setResponseCode(ResponseCode.RESPONSE_NULL);
            result.setDescription("No model efficient config found.");
            return result;
        }
        result.setData(response.getData());
        result.setDescription("success to find model efficient config");
        return result;
    }

    @Override
    public List<FilterUpdByMQTTConfig> fetchHasFirstRemind() {
        return modelEfficientConfigDao.queryHasFirstRemind();
    }

    @Override
    public ResultData updateByModelId(Map<String, Object> condition){
        ResultData result = new ResultData();
        ResultData response = modelEfficientConfigDao.updateByModelId(condition);
        if(response.getResponseCode()==ResponseCode.RESPONSE_ERROR){
            result.setResponseCode(ResponseCode.RESPONSE_ERROR);
            result.setDescription("Fail to update efficient by modelId");
            return result;
        }
        result.setResponseCode(ResponseCode.RESPONSE_OK);
        result.setDescription("success to update efficient by modelId");
        return result;
    }
}
