package finley.gmair.service;

import finley.gmair.model.machine.OutPm25Hourly;
import finley.gmair.util.ResultData;

import java.util.Map;

public interface OutPm25HourlyService {
    ResultData create(OutPm25Hourly outPm25Hourly);

    ResultData fetch(Map<String, Object> condition);

    ResultData updateByMachineId(Map<String, Object> condition);
}
