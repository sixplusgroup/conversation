package finley.gmair.service.impl;

import finley.gmair.dao.ExpressCompanyDao;
import finley.gmair.model.express.ExpressCompany;
import finley.gmair.service.ExpressService;
import finley.gmair.util.ResponseCode;
import finley.gmair.util.ResultData;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Map;

@Service
public class ExpressServiceImpl implements ExpressService {

    @Autowired
    private ExpressCompanyDao expressCompanyDao;

    @Override
    public ResultData createExpressCompany(ExpressCompany company) {
        ResultData result = new ResultData();
        ResultData response = expressCompanyDao.insertExpressCompany(company);
        if (response.getResponseCode() != ResponseCode.RESPONSE_OK) {
            result.setResponseCode(ResponseCode.RESPONSE_ERROR);
            result.setDescription(new StringBuffer("Fail to store express company with name: ").append(company.getCompanyName()).toString());
            return result;
        }
        result.setResponseCode(ResponseCode.RESPONSE_OK);
        result.setData(response.getData());
        return result;
    }

    @Override
    public ResultData fetchExpressCompany(Map<String, Object> condition) {
        ResultData result = new ResultData();
        ResultData response = expressCompanyDao.queryExpressCompany(condition);
        if (response.getResponseCode() == ResponseCode.RESPONSE_OK) {
            result.setResponseCode(ResponseCode.RESPONSE_OK);
            result.setData(response.getData());
        }
        if (response.getResponseCode() == ResponseCode.RESPONSE_NULL) {
            result.setResponseCode(ResponseCode.RESPONSE_NULL);
            result.setDescription("No express company found");
        }
        if (response.getResponseCode() == ResponseCode.RESPONSE_ERROR) {
            result.setResponseCode(ResponseCode.RESPONSE_ERROR);
            result.setDescription("Fail to fetch express company from database");
        }
        return result;
    }
}
