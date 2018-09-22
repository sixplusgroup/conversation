package finley.gmair.dao;

import finley.gmair.model.drift.Activity;
import finley.gmair.util.ResultData;

import java.util.Map;

public interface ActivityDao {
    ResultData queryActivity(Map<String, Object> condition);

    ResultData insertActivity(Activity activity);

    ResultData updateActivity(Map<String, Object> condition);
}
