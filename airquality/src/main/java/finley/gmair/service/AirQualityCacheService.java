package finley.gmair.service;

import finley.gmair.model.air.CityAirQuality;
import finley.gmair.util.ResultData;
import org.springframework.cache.annotation.CachePut;

import java.util.Map;

public interface AirQualityCacheService{

    @CachePut(value = "airQualityMap", key = "#airQuality.cityId", condition = "#airQuality != null ")
    default CityAirQuality generate(CityAirQuality airQuality){
        return airQuality;
    }

    CityAirQuality fetch(String cityId);
}
