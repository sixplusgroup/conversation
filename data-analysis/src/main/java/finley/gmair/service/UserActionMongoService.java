package finley.gmair.service;

import finley.gmair.model.dataAnalysis.UserActionDaily;
import finley.gmair.model.machine.UserAction;
import finley.gmair.util.ResultData;

import java.util.List;

public interface UserActionMongoService {
    ResultData getDailyStatisticalData();

    List<UserActionDaily> dealUserAction2Component(List<UserAction> list);
}
