package finley.gmair.service.impl;

import finley.gmair.dao.ExpressDao;
import finley.gmair.model.drift.Express;
import finley.gmair.service.ExpressService;
import finley.gmair.util.ResponseCode;
import finley.gmair.util.ResultData;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Map;

@Service
public class ExpressServiceImpl implements ExpressService {

    @Autowired
    private ExpressDao expressDao;

    @Override
    public ResultData fetchExpress(Map<String, Object> condition){
        ResultData result = new ResultData();
        ResultData response = expressDao.queryExpress(condition);
        if (response.getResponseCode() == ResponseCode.RESPONSE_NULL) {
            result.setResponseCode(ResponseCode.RESPONSE_NULL);
            result.setDescription("No express message found from database");
        }
        if (response.getResponseCode() == ResponseCode.RESPONSE_ERROR) {
            result.setResponseCode(ResponseCode.RESPONSE_ERROR);
            result.setDescription("Fail to retrieve express");
        }
        if (response.getResponseCode() == ResponseCode.RESPONSE_OK) {
            result.setResponseCode(ResponseCode.RESPONSE_OK);
            result.setData(response.getData());
        }
        return result;
    }

    @Override
    public ResultData createExpress(Express express){
        ResultData result = new ResultData();
        ResultData response = expressDao.insertExpress(express);
        if (response.getResponseCode() == ResponseCode.RESPONSE_ERROR) {
            result.setResponseCode(ResponseCode.RESPONSE_ERROR);
            result.setDescription("Fail to insert express message to database");
        }
        if (response.getResponseCode() == ResponseCode.RESPONSE_OK) {
            result.setResponseCode(ResponseCode.RESPONSE_OK);
            result.setData(response.getData());
        }
        return result;
    }

    @Override
    public ResultData modifyExpress(Map<String, Object> condition){
        ResultData result = new ResultData();
        ResultData response = expressDao.updateExpress(condition);
        if (response.getResponseCode() == ResponseCode.RESPONSE_ERROR) {
            result.setResponseCode(ResponseCode.RESPONSE_ERROR);
            result.setDescription("Fail to update express");
        }
        if (response.getResponseCode() == ResponseCode.RESPONSE_OK) {
            result.setResponseCode(ResponseCode.RESPONSE_OK);
            result.setDescription("Succeed to update express");
        }
        return result;
    }
}
