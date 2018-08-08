package finley.gmair.service;

import finley.gmair.util.ResultData;

import java.util.Map;

public interface MachinePm25Service {
    ResultData handleHourly();

    ResultData handleDaily();

    ResultData handleMonthly();

    ResultData fetchPartialLatestPm25(String uid, String name);

    ResultData fetchAveragePm25();

    ResultData fetchMachineHourlyPm25(Map<String,Object> condition);

    ResultData fetchMachineDailyPm25(Map<String,Object> condition);
}
