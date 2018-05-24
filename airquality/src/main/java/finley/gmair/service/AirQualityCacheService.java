package finley.gmair.service;

import finley.gmair.model.air.CityAirQuality;
import finley.gmair.util.ResultData;
import org.springframework.cache.annotation.CachePut;

import java.util.Map;

public interface AirQualityCacheService{

    @CachePut(value = "airQualityMap", key = "#airQuality.cityId")
    default void generate(CityAirQuality airQuality){};

    ResultData fetch(String cityId);
}
