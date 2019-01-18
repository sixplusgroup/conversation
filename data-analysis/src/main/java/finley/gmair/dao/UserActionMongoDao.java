package finley.gmair.dao;

import finley.gmair.model.machine.UserAction;
import finley.gmair.util.ResultData;

import java.util.List;
import java.util.Map;

public interface UserActionMongoDao {

    ResultData query(Map<String, Object> condition);

    ResultData queryUserActionByUserId(List<UserAction> list);

    ResultData queryUserActionByComponent(List<UserAction> list);
}
