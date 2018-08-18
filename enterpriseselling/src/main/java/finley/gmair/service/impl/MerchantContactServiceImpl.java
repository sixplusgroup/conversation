package finley.gmair.service.impl;

import finley.gmair.dao.MerchantContactDao;
import finley.gmair.model.enterpriseselling.MerchantContact;
import finley.gmair.service.MerchantContactService;
import finley.gmair.util.ResponseCode;
import finley.gmair.util.ResultData;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Map;

@Service
public class MerchantContactServiceImpl implements MerchantContactService {
    @Autowired
    private MerchantContactDao merchantContactDao;

    @Override
    public ResultData create(MerchantContact merchantContact){
        ResultData result = new ResultData();
        ResultData response = merchantContactDao.insert(merchantContact);
        if (response.getResponseCode() == ResponseCode.RESPONSE_ERROR) {
            result.setResponseCode(ResponseCode.RESPONSE_ERROR);
            result.setDescription(new StringBuffer("Fail to insert merchant contact into database.").toString());
            return result;
        }
        result.setData(response.getData());
        return result;
    }

    @Override
    public ResultData fetch(Map<String, Object> condition){
        ResultData result = new ResultData();
        ResultData response = merchantContactDao.query(condition);
        if (response.getResponseCode() == ResponseCode.RESPONSE_ERROR) {
            result.setResponseCode(ResponseCode.RESPONSE_ERROR);
            result.setDescription("Fail to fetch merchant contact from database.");
            return result;
        }
        else if (response.getResponseCode() == ResponseCode.RESPONSE_NULL) {
            result.setResponseCode(ResponseCode.RESPONSE_NULL);
            result.setDescription("No merchant contact found.");
            return result;
        }
        result.setData(response.getData());
        result.setDescription("success to find merchant contact");
        return result;
    }

    @Override
    public ResultData update(Map<String, Object> condition){
        ResultData result = new ResultData();
        ResultData response = merchantContactDao.update(condition);
        if(response.getResponseCode()==ResponseCode.RESPONSE_ERROR){
            result.setResponseCode(ResponseCode.RESPONSE_ERROR);
            result.setDescription("Fail to update merchant contact by modelId");
            return result;
        }
        result.setResponseCode(ResponseCode.RESPONSE_OK);
        result.setDescription("success to update merchant contact by modelId");
        return result;
    }
}
