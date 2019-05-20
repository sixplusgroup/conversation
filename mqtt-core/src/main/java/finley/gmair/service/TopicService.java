package finley.gmair.service;

import finley.gmair.model.mqtt.Topic;
import finley.gmair.util.ResultData;

import java.util.Map;

public interface TopicService {
    ResultData fetch(Map<String, Object> condition);

    ResultData create(Topic topic);

    ResultData modify(Map<String, Object> condition);
}
