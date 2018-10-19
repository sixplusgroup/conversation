package finley.gmair.service.impl;

import finley.gmair.dao.CaseLngLatDao;
import finley.gmair.model.formaldehyde.CaseLngLat;
import finley.gmair.service.CaseLngLatService;
import finley.gmair.util.ResponseCode;
import finley.gmair.util.ResultData;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Map;

@Service
public class CaseLngLatServiceImpl implements CaseLngLatService {

    @Autowired
    private CaseLngLatDao caseLngLatDao;

    @Override
    public ResultData create(CaseLngLat caseLngLat) {
        ResultData result = new ResultData();
        ResultData response = caseLngLatDao.insert(caseLngLat);
        if (response.getResponseCode() == ResponseCode.RESPONSE_ERROR) {
            result.setResponseCode(ResponseCode.RESPONSE_ERROR);
            result.setDescription(new StringBuffer("Fail to insert caseLngLat into database.").toString());
            return result;
        }
        result.setData(response.getData());
        return result;
    }

    @Override
    public ResultData fetch(Map<String, Object> condition) {
        ResultData result = new ResultData();
        ResultData response = caseLngLatDao.query(condition);
        if (response.getResponseCode() == ResponseCode.RESPONSE_ERROR) {
            result.setResponseCode(ResponseCode.RESPONSE_ERROR);
            result.setDescription("Fail to fetch from database.");
            return result;
        } else if (response.getResponseCode() == ResponseCode.RESPONSE_NULL) {
            result.setResponseCode(ResponseCode.RESPONSE_NULL);
            result.setDescription("not found.");
            return result;
        }
        result.setData(response.getData());
        result.setDescription("success to find");
        return result;
    }

}
