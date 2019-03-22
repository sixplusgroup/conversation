package finley.gmair.dao;

import finley.gmair.model.mqtt.ApiBound;
import finley.gmair.util.ResultData;

import java.util.Map;

public interface ApiBoundDao {
    ResultData insertApiBound(ApiBound bound);

    ResultData queryApiBound(Map<String, Object> condition);

    ResultData updateApiBound(Map<String, Object> condition);
}
