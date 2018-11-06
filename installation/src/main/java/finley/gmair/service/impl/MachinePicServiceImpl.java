package finley.gmair.service.impl;

import finley.gmair.dao.MachinePicDao;
import finley.gmair.dao.PicDao;
import finley.gmair.model.installation.MachinePic;
import finley.gmair.model.installation.Pic;
import finley.gmair.service.MachinePicService;
import finley.gmair.service.PicService;
import finley.gmair.util.ResponseCode;
import finley.gmair.util.ResultData;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Map;

@Service
public class MachinePicServiceImpl implements MachinePicService {
    @Autowired
    private MachinePicDao machinePicDao;

    @Override
    public ResultData createMachinePic(MachinePic machinePic) {
        ResultData result = new ResultData();
        ResultData response = machinePicDao.insertMachinePic(machinePic);
        if (response.getResponseCode() == ResponseCode.RESPONSE_OK) {
            result.setResponseCode(ResponseCode.RESPONSE_OK);
            result.setData(response.getData());
        } else if (response.getResponseCode() == ResponseCode.RESPONSE_ERROR) {
            result.setResponseCode(ResponseCode.RESPONSE_ERROR);
            result.setDescription("Fail to insert pic " + machinePic.toString());
        }
        return result;
    }

    @Override
    public ResultData fetchMachinePic(Map<String, Object> condition) {
        ResultData result = new ResultData();
        ResultData response = machinePicDao.queryMachinePic(condition);
        if (response.getResponseCode() == ResponseCode.RESPONSE_OK) {
            result.setResponseCode(ResponseCode.RESPONSE_OK);
            result.setData(response.getData());
            result.setDescription("Success to fetch machine pic");
        } else if (response.getResponseCode() == ResponseCode.RESPONSE_ERROR) {
            result.setResponseCode(ResponseCode.RESPONSE_ERROR);
            result.setDescription("Fail to fetch machine pic");
        } else if (response.getResponseCode() == ResponseCode.RESPONSE_NULL) {
            result.setResponseCode(ResponseCode.RESPONSE_NULL);
            result.setDescription("No machine pic found");
        }
        return result;
    }

    @Override
    public ResultData deleteMachinePic(Map<String, Object> condition) {
        ResultData result = new ResultData();
        ResultData response = machinePicDao.deleteMachinePic(condition);
        if (response.getResponseCode() == ResponseCode.RESPONSE_OK) {
            result.setResponseCode(ResponseCode.RESPONSE_OK);
            result.setData(response.getData());
            result.setDescription("Success to delete machine pic");
        } else if (response.getResponseCode() == ResponseCode.RESPONSE_ERROR) {
            result.setResponseCode(ResponseCode.RESPONSE_ERROR);
            result.setDescription("Fail to delete machine pic");
        }
        return result;
    }

}

