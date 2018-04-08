package finley.gmair.service;

import finley.gmair.model.air.AirQuality;
import finley.gmair.service.impl.SerialServiceImpl;
import finley.gmair.util.ResponseCode;
import finley.gmair.util.ResultData;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.Map;

@Service
public class SerialService implements SerialServiceImpl{

    private Map<String, AirQuality> map = new HashMap<>();

    @Cacheable("map")
    @Override
    public ResultData generate(AirQuality airQuality) {
        ResultData result = new ResultData();
        map.put(airQuality.getCityId(), airQuality);
        return result;
    }

    @Cacheable("map")
    @Override
    public ResultData generate(Map<String, AirQuality> map) {
        ResultData result = new ResultData();
        for (Map.Entry entry : map.entrySet()) {
            map.put((String) entry.getKey(), (AirQuality) entry.getValue());
        }
        return result;
    }

    @Cacheable("map")
    @Override
    public ResultData fetch(String cityId) {
        ResultData result = new ResultData();
        AirQuality airQuality = map.get(cityId);
        if (airQuality == null) {
            result.setResponseCode(ResponseCode.RESPONSE_NULL);
        } else {
            result.setData(airQuality);
        }
        return result;
    }
}
