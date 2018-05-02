package finley.gmair.service.impl;

import finley.gmair.dao.FileMapDao;
import finley.gmair.model.resource.FileMap;
import finley.gmair.service.FileMapService;
import finley.gmair.util.ResponseCode;
import finley.gmair.util.ResultData;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Map;

@Service
public class FileMapServiceImpl implements FileMapService {
    @Autowired
    private FileMapDao fileMapDao;

    @Override
    public ResultData createFileMap(FileMap tempFileMap){
        ResultData result = new ResultData();

        ResultData response=fileMapDao.insertFileMap(tempFileMap);
        if(response.getResponseCode() == ResponseCode.RESPONSE_OK){
            result.setResponseCode(ResponseCode.RESPONSE_OK);
            result.setData(response.getData());
        }
        else if(response.getResponseCode() == ResponseCode.RESPONSE_ERROR){
            result.setResponseCode(ResponseCode.RESPONSE_ERROR);
            result.setDescription("Fail to insert filemap" + tempFileMap.toString());
        }
        return result;
    }

    @Override
    public ResultData fetchFileMap(Map<String, Object> condition){
        ResultData result = new ResultData();
        ResultData response = fileMapDao.queryFileMap(condition);
        if (response.getResponseCode() == ResponseCode.RESPONSE_OK) {
            result.setResponseCode(ResponseCode.RESPONSE_OK);
            result.setData(response.getData());
            result.setDescription("Success to fetch filemap");
        }
        else if (response.getResponseCode() == ResponseCode.RESPONSE_NULL) {
            result.setResponseCode(ResponseCode.RESPONSE_NULL);
            result.setDescription("No filemap found");
        }
        else if (response.getResponseCode() == ResponseCode.RESPONSE_ERROR) {
            result.setResponseCode(ResponseCode.RESPONSE_ERROR);
            result.setDescription("Fail to fetch filemap");
        }
        return result;
    }
}
