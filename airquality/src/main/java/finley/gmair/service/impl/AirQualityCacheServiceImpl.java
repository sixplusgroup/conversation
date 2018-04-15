package finley.gmair.service.impl;

import finley.gmair.model.air.AirQuality;
import finley.gmair.service.AirQualityCacheService;
import finley.gmair.util.ResponseCode;
import finley.gmair.util.ResultData;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.Map;


@Service
public class AirQualityCacheServiceImpl implements AirQualityCacheService{

        //cache map to store city name and airQuality
        private Map<String, AirQuality> airQualityMap = new HashMap<>();

        @Cacheable("airQualityMap")
        @Override
        public ResultData generate(AirQuality airQuality) {
            ResultData result = new ResultData();
            airQualityMap.put(airQuality.getCityId(), airQuality);
            return result;
        }

        @Cacheable("airQualityMap")
        @Override
        public ResultData generate(Map<String, AirQuality> map) {
            ResultData result = new ResultData();
            for (Map.Entry entry : map.entrySet()) {
                    map.put((String) entry.getKey(), (AirQuality) entry.getValue());
            }
            return result;
        }

        @Cacheable("airQualityMap")
        @Override
        public ResultData fetch(String cityId) {
            ResultData result = new ResultData();
            AirQuality airQuality = airQualityMap.get(cityId);
            if (airQuality == null) {
                    result.setResponseCode(ResponseCode.RESPONSE_NULL);
            } else {
                    result.setData(airQuality);
            }
                return result;
        }



}
