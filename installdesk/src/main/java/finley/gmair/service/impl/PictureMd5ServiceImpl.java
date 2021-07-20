package finley.gmair.service.impl;

import finley.gmair.dao.PictureMd5Dao;
import finley.gmair.model.installation.PictureMd5;
import finley.gmair.service.PictureMd5Service;
import finley.gmair.util.ResponseCode;
import finley.gmair.util.ResultData;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Map;

@Service
public class PictureMd5ServiceImpl implements PictureMd5Service {

    @Autowired
    private PictureMd5Dao pictureMd5Dao;

    @Override
    public ResultData create(PictureMd5 pictureMd5) {
        ResultData result = new ResultData();
        ResultData response = pictureMd5Dao.insert(pictureMd5);
        if (response.getResponseCode() == ResponseCode.RESPONSE_ERROR) {
            result.setResponseCode(ResponseCode.RESPONSE_ERROR);
            result.setDescription(response.getDescription());
        } else {
            result.setData(response.getData());
        }
        return result;
    }

    @Override
    public ResultData fetch(Map<String, Object> condition) {
        ResultData result = new ResultData();
        ResultData response = pictureMd5Dao.query(condition);
        if (response.getResponseCode() == ResponseCode.RESPONSE_ERROR) {
            result.setResponseCode(ResponseCode.RESPONSE_ERROR);
            result.setDescription(response.getDescription());
        } else if (response.getResponseCode() == ResponseCode.RESPONSE_NULL) {
            result.setResponseCode(ResponseCode.RESPONSE_NULL);
        } else {
            result.setData(response.getData());
        }
        return result;
    }
}
