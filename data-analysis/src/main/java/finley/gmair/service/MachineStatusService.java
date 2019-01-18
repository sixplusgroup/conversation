package finley.gmair.service;

import finley.gmair.util.ResultData;

public interface MachineStatusService {
    ResultData handleHourlyStatisticalData();

    ResultData handleDailyStatisticalData();
}
