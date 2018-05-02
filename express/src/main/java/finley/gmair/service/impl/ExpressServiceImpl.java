package finley.gmair.service.impl;

import finley.gmair.dao.ExpressCompanyDao;
import finley.gmair.dao.ExpressOrderDao;
import finley.gmair.dao.ExpressParcelDao;
import finley.gmair.model.express.ExpressCompany;
import finley.gmair.model.express.ExpressOrder;
import finley.gmair.model.express.ExpressParcel;
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

    @Autowired
    private ExpressOrderDao expressOrderDao;

    @Autowired
    private ExpressParcelDao expressParcelDao;

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

    @Override
    public ResultData createExpressOrder(ExpressOrder order) {
        ResultData result = new ResultData();
        ResultData response = expressOrderDao.insertExpressOrder(order);
        if (response.getResponseCode() != ResponseCode.RESPONSE_OK) {
            result.setResponseCode(ResponseCode.RESPONSE_ERROR);
            result.setDescription(new StringBuffer("Fail to store express order with id: ").append(order.getExpressId()).toString());
            return result;
        }
        result.setResponseCode(ResponseCode.RESPONSE_OK);
        result.setData(response.getData());
        return result;
    }

    @Override
    public ResultData fetchExpressOrder(Map<String, Object> condition) {
        ResultData result = new ResultData();
        ResultData response = expressOrderDao.queryExpressOrder(condition);
        if (response.getResponseCode() == ResponseCode.RESPONSE_OK) {
            result.setResponseCode(ResponseCode.RESPONSE_OK);
            result.setData(response.getData());
        }
        if (response.getResponseCode() == ResponseCode.RESPONSE_NULL) {
            result.setResponseCode(ResponseCode.RESPONSE_NULL);
            result.setDescription("No express order found");
        }
        if (response.getResponseCode() == ResponseCode.RESPONSE_ERROR) {
            result.setResponseCode(ResponseCode.RESPONSE_ERROR);
            result.setDescription("Fail to fetch express order from database");
        }
        return result;
    }

    @Override
    public ResultData createExpressParcel(ExpressParcel expressParcel) {
        ResultData result = new ResultData();
        ResultData response = expressParcelDao.insertExpressParcel(expressParcel);
        if (response.getResponseCode() != ResponseCode.RESPONSE_OK) {
            result.setResponseCode(ResponseCode.RESPONSE_ERROR);
            result.setDescription(new StringBuffer("Fail to store express parcel with parent_express: ").append(expressParcel.getParentExpress()).toString());
            return result;
        }
        result.setResponseCode(ResponseCode.RESPONSE_OK);
        result.setData(response.getData());
        return result;
    }

    @Override
    public ResultData fetchExpressParcel(Map<String, Object> condition) {
        ResultData result = new ResultData();
        ResultData response = expressParcelDao.queryExpressParcel(condition);
        if (response.getResponseCode() == ResponseCode.RESPONSE_OK) {
            result.setResponseCode(ResponseCode.RESPONSE_OK);
            result.setData(response.getData());
        }
        if (response.getResponseCode() == ResponseCode.RESPONSE_NULL) {
            result.setResponseCode(ResponseCode.RESPONSE_NULL);
            result.setDescription("No express parcel found");
        }
        if (response.getResponseCode() == ResponseCode.RESPONSE_ERROR) {
            result.setResponseCode(ResponseCode.RESPONSE_ERROR);
            result.setDescription("Fail to fetch express parcel from database");
        }
        return result;
    }
}
