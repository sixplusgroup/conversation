package finley.gmair.service;

import finley.gmair.model.mqtt.ApiTopic;
import finley.gmair.util.ResultData;

import java.util.Map;

public interface TopicService {
    ResultData fetch(Map<String, Object> condition);

    ResultData create(ApiTopic topic);

    ResultData modify(Map<String, Object> condition);
}
