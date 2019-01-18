package finley.gmair.service;

import finley.gmair.model.dataAnalysis.UserActionDaily;
import finley.gmair.util.ResultData;

import java.util.List;
import java.util.Map;

public interface UserActionDailyService {
    ResultData insertBatchDaily(List<UserActionDaily> list);

    ResultData fetchDaily(Map<String, Object> condition);
}
