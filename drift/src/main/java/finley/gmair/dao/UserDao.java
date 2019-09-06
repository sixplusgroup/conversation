package finley.gmair.dao;

import finley.gmair.model.drift.DriftUser;
import finley.gmair.util.ResultData;

import java.util.Map;

public interface UserDao {
    ResultData query(Map<String, Object> condition);

    ResultData insert(DriftUser user);

    ResultData update(DriftUser user);
}
