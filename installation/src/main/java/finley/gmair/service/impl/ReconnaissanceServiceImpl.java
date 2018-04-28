package finley.gmair.service.impl;

import finley.gmair.dao.ReconnaissanceDao;
import finley.gmair.model.installation.Reconnaissance;
import finley.gmair.service.ReconnaissanceService;
import finley.gmair.util.ResponseCode;
import finley.gmair.util.ResultData;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Map;

@Service
public class ReconnaissanceServiceImpl implements ReconnaissanceService {

    @Autowired
    private ReconnaissanceDao reconnaissanceDao;

    @Override
    public ResultData createReconnaissance(Reconnaissance reconnaissance) {
        ResultData result = new ResultData();
        ResultData response = reconnaissanceDao.insert(reconnaissance);
        if (response.getResponseCode() == ResponseCode.RESPONSE_OK) {
            result.setResponseCode(ResponseCode.RESPONSE_OK);
            result.setData(response.getData());
        } else {
            result.setResponseCode(ResponseCode.RESPONSE_ERROR);
            result.setDescription("Fail to create reconnaissance");
        }
        return result;
    }

    @Override
    public ResultData fetchReconnaissance(Map<String, Object> condition) {
        ResultData result = new ResultData();
        ResultData response = reconnaissanceDao.query(condition);
        if (response.getResponseCode() == ResponseCode.RESPONSE_OK) {
            result.setResponseCode(ResponseCode.RESPONSE_OK);
            result.setData(response.getData());
        }
        if (response.getResponseCode() == ResponseCode.RESPONSE_NULL) {
            result.setResponseCode(ResponseCode.RESPONSE_NULL);
            result.setDescription("No reconnaissance found");
        }
        if (response.getResponseCode() == ResponseCode.RESPONSE_ERROR) {

        }
        return result;
    }
}
