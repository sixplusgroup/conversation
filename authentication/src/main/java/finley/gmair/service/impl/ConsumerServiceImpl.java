package finley.gmair.service.impl;

import finley.gmair.dao.AddressDao;
import finley.gmair.dao.ConsumerDao;
import finley.gmair.dao.PhoneDao;
import finley.gmair.model.consumer.Address;
import finley.gmair.model.consumer.Consumer;
import finley.gmair.model.consumer.Phone;
import finley.gmair.service.ConsumerService;
import finley.gmair.util.ResponseCode;
import finley.gmair.util.ResultData;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

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
    public ResultData queryConsumer(Map<String, Object> condition) {
        ResultData result = new ResultData();

        return result;
    }
}
