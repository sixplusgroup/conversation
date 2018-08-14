package finley.gmair.service.impl;

import finley.gmair.dao.EnterpriseOrderDao;
import finley.gmair.model.order.EnterpriseOrder;
import finley.gmair.service.EnterpriseOrderService;
import finley.gmair.util.ResponseCode;
import finley.gmair.util.ResultData;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;

@Service
public class EnterpriseOrderServiceImpl implements EnterpriseOrderService {
    @Autowired
    private EnterpriseOrderDao enterpriseOrderDao;

    @Override
    public ResultData create(EnterpriseOrder enterpriseOrder){
        ResultData result = new ResultData();
        ResultData response = enterpriseOrderDao.insert(enterpriseOrder);
        if (response.getResponseCode() == ResponseCode.RESPONSE_ERROR) {
            result.setResponseCode(ResponseCode.RESPONSE_ERROR);
            result.setDescription(new StringBuffer("Fail to insert into database.").toString());
            return result;
        }
        result.setData(response.getData());
        return result;
    }

    @Override
    public ResultData createBatch(List<EnterpriseOrder> list){
        ResultData result = new ResultData();
        ResultData response = enterpriseOrderDao.insertBatch(list);
        if(response.getResponseCode() == ResponseCode.RESPONSE_ERROR){
            result.setResponseCode(ResponseCode.RESPONSE_ERROR);
            result.setDescription("fail to create batch order");
            return result;
        }
        result.setDescription("success to create batch order");
        return result;
    }

    @Override
    public ResultData fetch(Map<String, Object> condition){
        ResultData result = new ResultData();
        ResultData response = enterpriseOrderDao.query(condition);
        if (response.getResponseCode() == ResponseCode.RESPONSE_ERROR) {
            result.setResponseCode(ResponseCode.RESPONSE_ERROR);
            result.setDescription("Fail to fetch enterprise order.");
            return result;
        }
        else if (response.getResponseCode() == ResponseCode.RESPONSE_NULL) {
            result.setResponseCode(ResponseCode.RESPONSE_NULL);
            result.setDescription("No enterprise order found.");
            return result;
        }
        result.setData(response.getData());
        result.setDescription("success to find model light");
        return result;
    }

    @Override
    public ResultData update(Map<String, Object> condition){
        ResultData result = new ResultData();
        ResultData response = enterpriseOrderDao.update(condition);
        if(response.getResponseCode()==ResponseCode.RESPONSE_ERROR){
            result.setResponseCode(ResponseCode.RESPONSE_ERROR);
            result.setDescription("Fail to update");
            return result;
        }
        result.setResponseCode(ResponseCode.RESPONSE_OK);
        result.setDescription("success to update");
        return result;
    }
}
