package finley.gmair.service.impl;

import finley.gmair.dao.PicDao;
import finley.gmair.model.installation.Pic;
import finley.gmair.service.PicService;
import finley.gmair.util.ResponseCode;
import finley.gmair.util.ResultData;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Map;

@Service
public class PicServiceImpl implements PicService {
    @Autowired
    private PicDao picDao;

    @Override
    public ResultData createPic(Pic pic) {
        ResultData result = new ResultData();
        ResultData response = picDao.insertPic(pic);
        if (response.getResponseCode() == ResponseCode.RESPONSE_OK) {
            result.setResponseCode(ResponseCode.RESPONSE_OK);
            result.setData(response.getData());
        } else if (response.getResponseCode() == ResponseCode.RESPONSE_ERROR) {
            result.setResponseCode(ResponseCode.RESPONSE_ERROR);
            result.setDescription("Fail to insert pic" + pic.toString());
        }
        return result;
    }

    @Override
    public ResultData fetchPic(Map<String, Object> condition) {
        ResultData result = new ResultData();
        ResultData response = picDao.queryPic(condition);
        if (response.getResponseCode() == ResponseCode.RESPONSE_OK) {
            result.setResponseCode(ResponseCode.RESPONSE_OK);
            result.setData(response.getData());
            result.setDescription("Success to fetch pic");
        } else if (response.getResponseCode() == ResponseCode.RESPONSE_ERROR) {
            result.setResponseCode(ResponseCode.RESPONSE_ERROR);
            result.setDescription("Fail to fetch pic");
        } else if (response.getResponseCode() == ResponseCode.RESPONSE_NULL) {
            result.setResponseCode(ResponseCode.RESPONSE_NULL);
            result.setDescription("No pic found");
        }
        return result;
    }

    @Override
    public ResultData deletePic(Map<String, Object> condition) {
        ResultData result = new ResultData();
        ResultData response = picDao.deletePic(condition);
        if (response.getResponseCode() == ResponseCode.RESPONSE_OK) {
            result.setResponseCode(ResponseCode.RESPONSE_OK);
            result.setData(response.getData());
            result.setDescription("Success to delete pic");
        } else if (response.getResponseCode() == ResponseCode.RESPONSE_ERROR) {
            result.setResponseCode(ResponseCode.RESPONSE_ERROR);
            result.setDescription("Fail to delete pic");
        }
        return result;
    }

    @Override
    public ResultData updatePic(Pic pic) {
        ResultData result = new ResultData();
        ResultData response = picDao.updatePic(pic);
        if (response.getResponseCode() == ResponseCode.RESPONSE_OK) {
            result.setResponseCode(ResponseCode.RESPONSE_OK);
            result.setData(response.getData());
            result.setDescription("Success to update pic");
        } else if (response.getResponseCode() == ResponseCode.RESPONSE_ERROR) {
            result.setResponseCode(ResponseCode.RESPONSE_ERROR);
            result.setDescription("Fail to update pic");
        }
        return result;
    }
}

