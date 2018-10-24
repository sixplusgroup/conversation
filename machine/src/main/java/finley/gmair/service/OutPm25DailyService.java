package finley.gmair.service;

import finley.gmair.model.machine.OutPm25Daily;
import finley.gmair.util.ResultData;

import java.util.Map;

public interface OutPm25DailyService {
    ResultData create(OutPm25Daily outPm25Daily);

    ResultData fetch(Map<String, Object> condition);

}
