package finley.gmair.service;

import finley.gmair.util.ResultData;

public interface MachinePm25Service {
    ResultData handleHourly();
    ResultData handleDaily();
    ResultData handleMonthly();
}
