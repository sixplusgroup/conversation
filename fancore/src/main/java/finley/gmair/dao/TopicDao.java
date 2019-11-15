package finley.gmair.dao;

import finley.gmair.model.mqtt.Topic;
import finley.gmair.util.ResultData;

import java.util.Map;

public interface TopicDao {
    ResultData query(Map<String, Object> condition);

    ResultData insert(Topic topic);
    
    ResultData update(Map<String, Object> condition);
}
