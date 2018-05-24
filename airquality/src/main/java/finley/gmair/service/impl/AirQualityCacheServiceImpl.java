package finley.gmair.service.impl;

import finley.gmair.model.air.CityAirQuality;
import finley.gmair.service.AirQualityCacheService;
import finley.gmair.util.ResponseCode;
import finley.gmair.util.ResultData;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.Map;


@Service
public class AirQualityCacheServiceImpl implements AirQualityCacheService{

        @Cacheable({"airQualityMap"})
        @Override
        public ResultData fetch(String cityId) {
            ResultData result = new ResultData();
            return result;
        }



}
