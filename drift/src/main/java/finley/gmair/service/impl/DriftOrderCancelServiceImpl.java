package finley.gmair.service.impl;

import finley.gmair.dao.DriftOrderCancelDao;
import finley.gmair.model.drift.DriftAddress;
import finley.gmair.model.drift.DriftOrderCancel;
import finley.gmair.service.DriftOrderCancelService;
import finley.gmair.util.ResponseCode;
import finley.gmair.util.ResultData;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Map;

@Service
public class DriftOrderCancelServiceImpl implements DriftOrderCancelService {

    @Autowired
    private DriftOrderCancelDao driftOrderCancelDao;

    @Override
    public ResultData createCancel(DriftOrderCancel driftOrderCancel) {
        ResultData result = new ResultData();
        ResultData response = driftOrderCancelDao.insert(driftOrderCancel);
        if (response.getResponseCode() != ResponseCode.RESPONSE_OK) {
            result.setResponseCode(ResponseCode.RESPONSE_ERROR);
            result.setDescription("create error");
            return result;
        }
        result.setResponseCode(ResponseCode.RESPONSE_OK);
        result.setData(response.getData());
        return result;
    }

    @Override
    public ResultData fetchCancel(Map<String, Object> condition) {
        ResultData result = new ResultData();
        ResultData response = driftOrderCancelDao.query(condition);
        if (response.getResponseCode() == ResponseCode.RESPONSE_NULL) {
            result.setResponseCode(ResponseCode.RESPONSE_NULL);
            result.setDescription("No drift cancel record found from database");
        }
        if (response.getResponseCode() == ResponseCode.RESPONSE_ERROR) {
            result.setResponseCode(ResponseCode.RESPONSE_ERROR);
            result.setDescription("Fail to retrieve drift cancel record from database");
        }
        if (response.getResponseCode() == ResponseCode.RESPONSE_OK) {
            result.setResponseCode(ResponseCode.RESPONSE_OK);
            result.setData(response.getData());
        }
        return result;
    }

    @Override
    public ResultData updateCancel(DriftOrderCancel driftOrderCancel) {
        ResultData result = new ResultData();
        ResultData response = driftOrderCancelDao.update(driftOrderCancel);
        if (response.getResponseCode() != ResponseCode.RESPONSE_OK) {
            result.setResponseCode(ResponseCode.RESPONSE_ERROR);
            result.setDescription("Fail to update drift order cancel");
            return result;
        }
        result.setResponseCode(ResponseCode.RESPONSE_OK);
        result.setData(response.getData());
        return result;
    }
}
