package finley.gmair.dao;

import finley.gmair.model.machine.LatestPM2_5;
import finley.gmair.util.ResultData;

import java.util.Map;

public interface LatestPM2_5Dao {
    ResultData insert(LatestPM2_5 latestPM2_5);

    ResultData query(Map<String, Object> condition);

    ResultData updateByMachineId(Map<String, Object> condition);
}
