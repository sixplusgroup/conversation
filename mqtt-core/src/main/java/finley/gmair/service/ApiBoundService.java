package finley.gmair.service;

import finley.gmair.model.mqtt.ApiBound;
import finley.gmair.util.ResultData;

import java.util.Map;

public interface ApiBoundService {
    ResultData createApiBound(ApiBound bound);

    ResultData fetchApiBound(Map<String, Object> condition);

    ResultData modifyApiBound(Map<String, Object> condition);
}
