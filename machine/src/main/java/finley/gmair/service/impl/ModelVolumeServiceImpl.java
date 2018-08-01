package finley.gmair.service.impl;

import finley.gmair.dao.ModelVolumeDao;
import finley.gmair.model.machine.ModelVolume;
import finley.gmair.service.ModelVolumeService;
import finley.gmair.util.ResponseCode;
import finley.gmair.util.ResultData;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Map;

@Service
public class ModelVolumeServiceImpl implements ModelVolumeService {
    @Autowired
    private ModelVolumeDao modelVolumeDao;

    @Override
    public ResultData create(ModelVolume modelVolume){
        ResultData result = new ResultData();
        ResultData response = modelVolumeDao.insert(modelVolume);
        if (response.getResponseCode() == ResponseCode.RESPONSE_ERROR) {
            result.setResponseCode(ResponseCode.RESPONSE_ERROR);
            result.setDescription(new StringBuffer("Fail to insert model volume into database.").toString());
            return result;
        }
        result.setData(response.getData());
        return result;
    }

    @Override
    public ResultData fetch(Map<String, Object> condition){
        ResultData result = new ResultData();
        ResultData response = modelVolumeDao.query(condition);
        if (response.getResponseCode() == ResponseCode.RESPONSE_ERROR) {
            result.setResponseCode(ResponseCode.RESPONSE_ERROR);
            result.setDescription("Fail to fetch model volume.");
            return result;
        }
        else if (response.getResponseCode() == ResponseCode.RESPONSE_NULL) {
            result.setResponseCode(ResponseCode.RESPONSE_NULL);
            result.setDescription("No model volume found.");
            return result;
        }
        result.setData(response.getData());
        result.setDescription("success to find model volume");
        return result;
    }

    @Override
    public ResultData updateByModelId(Map<String, Object> condition){
        ResultData result = new ResultData();
        ResultData response = modelVolumeDao.updateByModelId(condition);
        if(response.getResponseCode()==ResponseCode.RESPONSE_ERROR){
            result.setResponseCode(ResponseCode.RESPONSE_ERROR);
            result.setDescription("Fail to update volume by modelId");
            return result;
        }
        result.setResponseCode(ResponseCode.RESPONSE_OK);
        result.setDescription("success to update volume by modelId");
        return result;
    }
}
