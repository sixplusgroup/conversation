package finley.gmair.service.impl;

import finley.gmair.dao.AddressDao;
import finley.gmair.model.drift.DriftAddress;
import finley.gmair.service.AddressService;
import finley.gmair.util.ResponseCode;
import finley.gmair.util.ResultData;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
public class AddressServiceImpl implements AddressService {

    @Autowired
    private AddressDao addressDao;

    @Override
    public ResultData createAddress(DriftAddress address) {
        ResultData result = new ResultData();
        if (address.getDefaultAddress() == 1) {
            Map<String, Object> condition = new HashMap<>();
            condition.put("consumerId", address.getConsumerId());
            condition.put("blockFlag", false);
            ResultData response = addressDao.query(condition);
            if (response.getResponseCode() == ResponseCode.RESPONSE_OK) {
                List<DriftAddress> list = (List<DriftAddress>) response.getData();
                new Thread(() -> {
                    for (DriftAddress da : list) {
                        if (da.getDefaultAddress() == 1) {
                            da.setDefaultAddress(0);
                            addressDao.update(da);
                        }
                    }
                }).start();
            }
        }
        ResultData response = addressDao.insert(address);
        if (response.getResponseCode() != ResponseCode.RESPONSE_OK) {
            result.setResponseCode(ResponseCode.RESPONSE_ERROR);
            result.setDescription("创建用户地址失败，请稍后重试");
            return result;
        }
        result.setResponseCode(ResponseCode.RESPONSE_OK);
        result.setData(response.getData());
        return result;
    }

    @Override
    public ResultData fetchAddress(Map<String, Object> condition) {
        ResultData result = new ResultData();
        ResultData response = addressDao.query(condition);
        if (response.getResponseCode() == ResponseCode.RESPONSE_NULL) {
            result.setResponseCode(ResponseCode.RESPONSE_NULL);
            result.setDescription("No drift address found from database");
        }
        if (response.getResponseCode() == ResponseCode.RESPONSE_ERROR) {
            result.setResponseCode(ResponseCode.RESPONSE_ERROR);
            result.setDescription("Fail to retrieve drift address from database");
        }
        if (response.getResponseCode() == ResponseCode.RESPONSE_OK) {
            result.setResponseCode(ResponseCode.RESPONSE_OK);
            result.setData(response.getData());
        }
        return result;
    }

    @Override
    public ResultData updateAddress(DriftAddress address) {
        ResultData result = new ResultData();
        ResultData response = addressDao.update(address);
        if (response.getResponseCode() != ResponseCode.RESPONSE_OK) {
            result.setResponseCode(ResponseCode.RESPONSE_ERROR);
            result.setDescription("Fail to update drift address");
            return result;
        }
        result.setResponseCode(ResponseCode.RESPONSE_OK);
        result.setData(response.getData());
        return result;
    }
}
