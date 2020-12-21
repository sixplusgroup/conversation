package finley.gmair.service.impl;

import finley.gmair.dao.AddressDao;
import finley.gmair.dao.ConsumerDao;
import finley.gmair.dao.PhoneDao;
import finley.gmair.form.consumer.ConsumerPartInfoQuery;
import finley.gmair.model.consumer.Address;
import finley.gmair.model.consumer.Consumer;
import finley.gmair.model.consumer.Phone;
import finley.gmair.service.ConsumerService;
import finley.gmair.util.ResponseCode;
import finley.gmair.util.ResultData;
import finley.gmair.vo.consumer.ConsumerPartInfoVo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
public class ConsumerServiceImpl implements ConsumerService {
    @Autowired
    private ConsumerDao consumerDao;

    @Autowired
    private PhoneDao phoneDao;

    @Autowired
    private AddressDao addressDao;

    @Override
    @Transactional
    public ResultData createConsumer(Consumer consumer) {
        ResultData result = new ResultData();
        //save consumer data
        ResultData response = consumerDao.insert(consumer);
        if (response.getResponseCode() != ResponseCode.RESPONSE_OK) {
            result.setResponseCode(ResponseCode.RESPONSE_ERROR);
            result.setDescription("Fail to store consumer information");
            return result;
        }
        consumer = (Consumer) response.getData();
        //save consumer mobile, since the user is new, the phone will be marked as preferred
        Phone phone = consumer.getPhone();
        phone.setPreferred(true);
        response = phoneDao.insert(phone, consumer.getConsumerId());
        if(response.getResponseCode() != ResponseCode.RESPONSE_OK) {
            result.setResponseCode(ResponseCode.RESPONSE_ERROR);
            result.setDescription("Fail to store mobile information");
            return result;
        }
        consumer.setPhone((Phone) response.getData());
        //save consumer address, the same with phone, will be marked as preferred
        Address address = consumer.getAddress();
        address.setPreferred(true);
        response = addressDao.insert(address, consumer.getConsumerId());
        if(response.getResponseCode() != ResponseCode.RESPONSE_OK) {
            result.setResponseCode(ResponseCode.RESPONSE_ERROR);
            result.setDescription("Fail to store address information");
            return result;
        }
        consumer.setAddress((Address) response.getData());
        result.setData(consumer);
        return result;
    }

    @Override
    public ResultData fetchConsumer(Map<String, Object> condition) {
        ResultData result = new ResultData();
        ResultData response = consumerDao.query(condition);
        if(response.getResponseCode() == ResponseCode.RESPONSE_ERROR) {
            result.setResponseCode(ResponseCode.RESPONSE_ERROR);
            result.setDescription("Fail to query consumer information");
            return result;
        }
        if(response.getResponseCode() == ResponseCode.RESPONSE_NULL) {
            result.setResponseCode(ResponseCode.RESPONSE_NULL);
            result.setDescription("No qualified user is found");
            return result;
        }
        result.setData(response.getData());
        return result;
    }

    @Override
    public List<ConsumerPartInfoVo> fetchConsumerAccounts(ConsumerPartInfoQuery query) {
        return consumerDao.queryConsumerAccounts(query);
    }

    @Override
    public ResultData modifyConsumer(Map<String, Object> condition) {
        ResultData result = new ResultData();
        ResultData response = consumerDao.update(condition);
        if (response.getResponseCode() == ResponseCode.RESPONSE_OK) {
            result.setResponseCode(ResponseCode.RESPONSE_OK);
            result.setData(response.getData());
        }
        if (response.getResponseCode() == ResponseCode.RESPONSE_ERROR) {
            result.setResponseCode(ResponseCode.RESPONSE_ERROR);
            result.setDescription("Fail to update consumer information");
        }
        return result;
    }

    @Override
    public ResultData fetchConsumerAddress(Map<String, Object> condition) {
        return addressDao.query(condition);
    }

    @Override
    public ResultData modifyConsumerAddress(Map<String, Object> condition) {
        ResultData result = new ResultData();
        ResultData response = addressDao.update(condition);
        if (response.getResponseCode() == ResponseCode.RESPONSE_OK) {
            result.setResponseCode(ResponseCode.RESPONSE_OK);
            result.setData(response.getData());
        }
        if (response.getResponseCode() == ResponseCode.RESPONSE_ERROR) {
            result.setResponseCode(ResponseCode.RESPONSE_ERROR);
            result.setDescription("Fail to update consumer address information");
        }
        return result;
    }

    @Override
    public ResultData createConsumerAddress(Address address, String consumerId) {
        ResultData result = new ResultData();
        ResultData response = addressDao.insert(address, consumerId);
        if (response.getResponseCode() != ResponseCode.RESPONSE_OK) {
            result.setResponseCode(ResponseCode.RESPONSE_ERROR);
            result.setDescription("Fail to store address information");
            return result;
        }
        result.setResponseCode(ResponseCode.RESPONSE_OK);
        result.setData(response.getData());
        return result;
    }


    @Override
    public ResultData fetchConsumerPhone(Map<String, Object> condition){
        ResultData result = new ResultData();
        ResultData response = phoneDao.query(condition);
        if(response.getResponseCode() == ResponseCode.RESPONSE_OK){
            result.setResponseCode(ResponseCode.RESPONSE_OK);
            result.setDescription("ok");
            result.setData(response.getData());
            return result;
        }else if(response.getResponseCode()==ResponseCode.RESPONSE_NULL){
            result.setResponseCode(ResponseCode.RESPONSE_NULL);
            result.setDescription("null");
            return result;
        }else{
            result.setResponseCode(ResponseCode.RESPONSE_ERROR);
            result.setDescription("error");
            return result;
        }
    }

    @Override
    public ResultData modifyConsumerPhone(Map<String, Object> condition){
        ResultData result = new ResultData();
        ResultData response = phoneDao.update(condition);
        if(response.getResponseCode() != ResponseCode.RESPONSE_OK){
            result.setResponseCode(ResponseCode.RESPONSE_ERROR);
            result.setDescription("Fail to modify consumer phone");
            return result;
        }
        result.setResponseCode(ResponseCode.RESPONSE_OK);
        result.setData(response.getData());
        return result;
    }

    /**
     * This method is to query any result that match at least one of the conditions
     * @param condition
     * @return
     */
    @Override
    public boolean exist(Map<String, Object> condition) {
        Map<String, Object> con = new HashMap<>();
        for (Map.Entry<String, Object> e : condition.entrySet()) {
            con.clear();
            con.put(e.getKey(), e.getValue());
            ResultData response = consumerDao.query(con);
            if(response.getResponseCode() == ResponseCode.RESPONSE_OK) {
                return true;
            }
        }
        return false;
    }
}
