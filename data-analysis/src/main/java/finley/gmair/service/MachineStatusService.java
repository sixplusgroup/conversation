package finley.gmair.service;

import finley.gmair.util.ResultData;

public interface MachineStatusService {
    ResultData getHourlyStatisticalData();
    ResultData handleDailyStatisticalData();
}
