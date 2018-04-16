package finley.gmair.dao;

import finley.gmair.model.consumer.Consumer;
import finley.gmair.util.ResultData;
import org.springframework.transaction.annotation.Transactional;

import java.util.Map;

public interface ConsumerDao {
    @Transactional
    ResultData insert(Consumer consumer);

    ResultData query(Map<String, Object> condition);

    ResultData update(Consumer consumer);
}
