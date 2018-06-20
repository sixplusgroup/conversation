package finley.gmair.service.impl;

import finley.gmair.dao.ExpressCompanyDao;
import finley.gmair.dao.ExpressOrderDao;
import finley.gmair.dao.ExpressParcelDao;
import finley.gmair.model.express.*;
import finley.gmair.service.ExpressService;
import finley.gmair.util.ResponseCode;
import finley.gmair.util.ResultData;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.sql.Timestamp;

import java.util.HashMap;
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
    public ResultData createExpressOrder(ExpressOrder order, String[] qrcodeList) {
        ResultData result = new ResultData();
        ResultData response = expressOrderDao.insertExpressOrder(order);
        if (response.getResponseCode() != ResponseCode.RESPONSE_OK) {
            result.setResponseCode(ResponseCode.RESPONSE_ERROR);
            result.setDescription(new StringBuilder("Fail to store express order with id: ").append(order.getExpressId()).toString());
            return result;
        }
        ExpressOrder expressOrder = (ExpressOrder) response.getData();
        for (String qrcode : qrcodeList) {
            ExpressParcel parcel = new ExpressParcel();
            parcel.setCodeValue(qrcode);
            parcel.setExpressStatus(ExpressStatus.ASSIGNED);
            parcel.setParentExpress(expressOrder.getExpressId());
            parcel.setExpressNo(expressOrder.getExpressNo());
            parcel.setParcelType(ParcelType.MACHINE);
            response = expressParcelDao.insertExpressParcel(parcel);
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
    @Transactional
    public ResultData confirmReceive(String expressId) {
        ResultData result = new ResultData();
        Map<String, Object> condition = new HashMap<>();
        condition.put("expressId", expressId);
        condition.put("expressStatus", ExpressStatus.RECEIVED.getValue());
        condition.put("receiveTime", new Timestamp(System.currentTimeMillis()));
        //update express order status to received
        ResultData response = expressOrderDao.updateSingleExpressOrder(condition);
        if (response.getResponseCode() == ResponseCode.RESPONSE_ERROR) {
            result.setResponseCode(ResponseCode.RESPONSE_ERROR);
            result.setDescription("Fail to set order express status to received.");
            System.out.println(new StringBuffer("Fail to set order ").append(expressId).append(" express status to received."));
            return result;
        }
        //update express parcel status to received
        condition.put("parentExpress", expressId);
        condition.put("expressStatus", ExpressStatus.RECEIVED.getValue());
        response = expressParcelDao.updateSingle(condition);
        if (response.getResponseCode() == ResponseCode.RESPONSE_ERROR) {
            result.setResponseCode(ResponseCode.RESPONSE_ERROR);
            result.setDescription("Fail to set parcel express status to received.");
            System.out.println(new StringBuffer("Fail to set parcel express status of parent express: ").append(expressId).append(" to received."));
            return result;
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
