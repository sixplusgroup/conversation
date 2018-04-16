package finley.gmair.service;

import finley.gmair.model.consumer.Consumer;
import finley.gmair.util.ResultData;
import org.springframework.transaction.annotation.Transactional;

import java.util.Map;

public interface ConsumerService {
    @Transactional
    ResultData createConsumer(Consumer consumer);

    ResultData fetchConsumer(Map<String, Object> condition);

    boolean existConsumer(Map<String, Object> condition);
}
