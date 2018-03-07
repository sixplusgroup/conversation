package finley.gmair.service;

import finley.gmair.model.consumer.Consumer;
import finley.gmair.util.ResultData;

public interface ConsumerService {
    ResultData createConsumer(Consumer consumer);
}
