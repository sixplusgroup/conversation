package finley.gmair.service;

import finley.gmair.util.ResultData;

import java.util.Map;

public interface AirQualityStatisticService {

    ResultData handleAirQualityHourlyStatistic();
    ResultData handleAirQualityDailyStatistic();
    ResultData handleAirQualityMonthlyStatistic();

    ResultData fetchAirQualityHourlyStatistic(Map<String, Object> condition);
    ResultData fetchAirQualityDailyStatistic(Map<String, Object> condition);
    ResultData fetchAirQualityMonthlyStatistic(Map<String, Object> condition);
}
