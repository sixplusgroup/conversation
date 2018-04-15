package finley.gmair.service;

import finley.gmair.model.air.AirQuality;
import finley.gmair.util.ResultData;

import java.util.Map;

public interface AirQualityCacheService{

    ResultData generate(AirQuality airQuality);

    ResultData generate(Map<String, AirQuality> map);

    ResultData fetch(String cityId);
}
