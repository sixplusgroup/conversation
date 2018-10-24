package finley.gmair.dao;

import finley.gmair.model.machine.OutPm25Hourly;
import finley.gmair.util.ResultData;

import java.util.Map;

public interface OutPm25HourlyDao {
    ResultData insert(OutPm25Hourly outPm25Hourly);

    ResultData query(Map<String, Object> condition);

    ResultData updateByMachineId(Map<String, Object> condition);
}
