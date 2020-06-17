package finley.gmair.dao;

import finley.gmair.model.analysis.UserActionDaily;
import finley.gmair.util.ResultData;

import java.util.List;
import java.util.Map;

public interface UserActionDailyDao {
    ResultData query(Map<String, Object> condition);

    ResultData insertBatch(List<UserActionDaily> list);
}
