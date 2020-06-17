package finley.gmair.service;

import finley.gmair.model.machine.UserAction;
import finley.gmair.util.ResultData;

import java.util.List;
import java.util.Map;

public interface UserActionMongoService {
    ResultData fetchData(Map<String, Object> condition);

    ResultData getDataGroupByUserId(List<UserAction> list);

    ResultData getDataGroupByComponent(List<UserAction> list);
}
