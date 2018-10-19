package finley.gmair.service.impl;

import finley.gmair.dao.CaseProfileDao;
import finley.gmair.model.formaldehyde.CaseProfile;
import finley.gmair.service.CaseProfileService;
import finley.gmair.util.ResponseCode;
import finley.gmair.util.ResultData;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Map;

@Service
public class CaseProfileServiceImpl implements CaseProfileService {
    @Autowired
    private CaseProfileDao caseProfileDao;

    @Override
    public ResultData create(CaseProfile caseProfile){
        ResultData result = new ResultData();
        ResultData response = caseProfileDao.insert(caseProfile);
        if (response.getResponseCode() == ResponseCode.RESPONSE_ERROR) {
            result.setResponseCode(ResponseCode.RESPONSE_ERROR);
            result.setDescription(new StringBuffer("Fail to insert caseProfile into database.").toString());
            return result;
        }
        result.setData(response.getData());
        return result;
    }

    @Override
    public ResultData fetch(Map<String, Object> condition){
        ResultData result = new ResultData();
        ResultData response = caseProfileDao.query(condition);
        if (response.getResponseCode() == ResponseCode.RESPONSE_ERROR) {
            result.setResponseCode(ResponseCode.RESPONSE_ERROR);
            result.setDescription("Fail to fetch caseProfile from database.");
            return result;
        }
        else if (response.getResponseCode() == ResponseCode.RESPONSE_NULL) {
            result.setResponseCode(ResponseCode.RESPONSE_NULL);
            result.setDescription("No caseProfile found.");
            return result;
        }
        result.setData(response.getData());
        result.setDescription("success to find caseProfile");
        return result;
    }

    @Override
    public ResultData update(Map<String, Object> condition){
        ResultData result = new ResultData();
        ResultData response = caseProfileDao.update(condition);
        if(response.getResponseCode()==ResponseCode.RESPONSE_ERROR){
            result.setResponseCode(ResponseCode.RESPONSE_ERROR);
            result.setDescription("Fail to update caseProfile");
            return result;
        }
        result.setResponseCode(ResponseCode.RESPONSE_OK);
        result.setDescription("success to update caseProfile");
        return result;
    }
}
