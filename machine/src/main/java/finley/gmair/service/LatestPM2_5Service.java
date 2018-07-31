package finley.gmair.service;

import finley.gmair.model.machine.LatestPM2_5;
import finley.gmair.util.ResultData;

import java.util.Map;

public interface LatestPM2_5Service {
    ResultData create(LatestPM2_5 latestPM2_5);

    ResultData fetch(Map<String, Object> condition);

    ResultData updateByMachineId(Map<String, Object> condition);
}
