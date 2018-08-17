package finley.gmair.service.impl;

import finley.gmair.dao.MerchantProfileDao;
import finley.gmair.model.enterpriseselling.MerchantProfile;
import finley.gmair.service.MerchantProfileService;
import finley.gmair.util.ResponseCode;
import finley.gmair.util.ResultData;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Map;

@Service
public class MerchantProfileServiceImpl implements MerchantProfileService {
    @Autowired
    private MerchantProfileDao merchantProfileDao;

    @Override
    public ResultData create(MerchantProfile merchantProfile){
        ResultData result = new ResultData();
        ResultData response = merchantProfileDao.insert(merchantProfile);
        if (response.getResponseCode() == ResponseCode.RESPONSE_ERROR) {
            result.setResponseCode(ResponseCode.RESPONSE_ERROR);
            result.setDescription(new StringBuffer("Fail to insert merchant profile into database.").toString());
            return result;
        }
        result.setData(response.getData());
        return result;
    }

    @Override
    public ResultData fetch(Map<String, Object> condition){
        ResultData result = new ResultData();
        ResultData response = merchantProfileDao.query(condition);
        if (response.getResponseCode() == ResponseCode.RESPONSE_ERROR) {
            result.setResponseCode(ResponseCode.RESPONSE_ERROR);
            result.setDescription("Fail to fetch merchant profile from database.");
            return result;
        }
        else if (response.getResponseCode() == ResponseCode.RESPONSE_NULL) {
            result.setResponseCode(ResponseCode.RESPONSE_NULL);
            result.setDescription("No merchant profile found.");
            return result;
        }
        result.setData(response.getData());
        result.setDescription("success to find merchant profile");
        return result;
    }

    @Override
    public ResultData update(Map<String, Object> condition){
        ResultData result = new ResultData();
        ResultData response = merchantProfileDao.update(condition);
        if(response.getResponseCode()==ResponseCode.RESPONSE_ERROR){
            result.setResponseCode(ResponseCode.RESPONSE_ERROR);
            result.setDescription("Fail to update merchant profile by modelId");
            return result;
        }
        result.setResponseCode(ResponseCode.RESPONSE_OK);
        result.setDescription("success to update merchant profile by modelId");
        return result;
    }
}
