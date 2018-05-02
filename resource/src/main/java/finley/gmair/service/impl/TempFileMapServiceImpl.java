package finley.gmair.service.impl;

import finley.gmair.dao.TempFileMapDao;
import finley.gmair.model.resource.FileMap;
import finley.gmair.service.TempFileMapService;
import finley.gmair.util.ResponseCode;
import finley.gmair.util.ResultData;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Map;

@Service
public class TempFileMapServiceImpl implements TempFileMapService {

    @Autowired
    private TempFileMapDao tempFileMapDao;

    @Override
    public ResultData createTempFileMap(FileMap tempFileMap){
        ResultData result = new ResultData();

        ResultData response=tempFileMapDao.insertTempFileMap(tempFileMap);
        if(response.getResponseCode() == ResponseCode.RESPONSE_OK){
            result.setResponseCode(ResponseCode.RESPONSE_OK);
            result.setData(response.getData());
        }
        else if(response.getResponseCode() == ResponseCode.RESPONSE_ERROR){
            result.setResponseCode(ResponseCode.RESPONSE_ERROR);
            result.setDescription("Fail to insert tempfilemap" + tempFileMap.toString());
        }
        return result;
    }

    @Override
    public ResultData fetchTempFileMap(Map<String, Object> condition){
        ResultData result = new ResultData();
        ResultData response = tempFileMapDao.queryTempFileMap(condition);
        if (response.getResponseCode() == ResponseCode.RESPONSE_OK) {
            result.setResponseCode(ResponseCode.RESPONSE_OK);
            result.setData(response.getData());
            result.setDescription("Success to fetch tempfilemap");
        }
        else if (response.getResponseCode() == ResponseCode.RESPONSE_NULL) {
            result.setResponseCode(ResponseCode.RESPONSE_NULL);
            result.setDescription("No tempfilemap found");
        }
        else if (response.getResponseCode() == ResponseCode.RESPONSE_ERROR) {
            result.setResponseCode(ResponseCode.RESPONSE_ERROR);
            result.setDescription("Fail to fetch tempfilemap");
        }
        return result;
    }
}
