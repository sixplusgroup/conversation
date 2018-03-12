package finley.gmair.service;

import finley.gmair.model.consumer.Consumer;
import finley.gmair.util.ResultData;

import java.util.Map;

public interface ConsumerService {
    ResultData createConsumer(Consumer consumer);

    ResultData queryConsumer(Map<String, Object> condition);
}
