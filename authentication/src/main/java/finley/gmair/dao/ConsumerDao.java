package finley.gmair.dao;

import finley.gmair.model.consumer.Consumer;
import finley.gmair.util.ResultData;

import java.util.Map;

public interface ConsumerDao {
    ResultData insert(Consumer consumer);

    ResultData query(Map<String, Object> condition);

    ResultData update(Consumer consumer);
}
