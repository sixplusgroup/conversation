package finley.gmair.service;

import finley.gmair.model.air.CityAirQuality;
import finley.gmair.util.ResultData;

import java.util.Map;

public interface AirQualityCacheService{

    ResultData generate(CityAirQuality airQuality);

    ResultData generate(Map<String, CityAirQuality> map);

    ResultData fetch(String cityId);
}
