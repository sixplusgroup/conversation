package finley.gmair.service;

import finley.gmair.model.consumer.Address;
import finley.gmair.model.consumer.Consumer;
import finley.gmair.model.consumer.Phone;
import finley.gmair.util.ResultData;
import org.springframework.transaction.annotation.Transactional;

import javax.xml.transform.Result;
import java.util.Map;

public interface ConsumerService {
    @Transactional
    ResultData createConsumer(Consumer consumer);

    ResultData fetchConsumer(Map<String, Object> condition);

    ResultData modifyConsumer(Map<String, Object> condition);

    ResultData fetchConsumerAddress(Map<String, Object> condition);

    ResultData modifyConsumerAddress(Map<String, Object> condition);

    ResultData createConsumerAddress(Address address, String consumerId);

    ResultData modifyConsumerPhone(Map<String, Object> condition);

    boolean exist(Map<String, Object> condition);
}
