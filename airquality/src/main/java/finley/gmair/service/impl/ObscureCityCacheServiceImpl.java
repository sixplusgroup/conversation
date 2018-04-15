package finley.gmair.service.impl;

import finley.gmair.service.ObscureCityCacheService;
import finley.gmair.util.ResponseCode;
import finley.gmair.util.ResultData;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.Map;

@Service
public class ObscureCityCacheServiceImpl implements ObscureCityCacheService {

    private Map<String, String> cityMap = new HashMap<>();

    @Cacheable("cityMap")
    @Override
    public ResultData generate(String cityName, String cityId) {
        ResultData result = new ResultData();
        cityMap.put(cityName, cityId);
        return result;
    }

    @Cacheable("cityMap")
    @Override
    public ResultData generate(Map<String, String> map) {
        ResultData result = new ResultData();
        for (Map.Entry entry : map.entrySet()) {
            cityMap.put((String) entry.getKey(), (String) entry.getValue());
        }
        return result;
    }

    @Cacheable("cityMap")
    @Override
    public ResultData fetch(String cityName) {
        ResultData result = new ResultData();
        if (cityMap.get(cityName) == null) {
            result.setResponseCode(ResponseCode.RESPONSE_NULL);
        } else {
            result.setData(cityMap.get(cityName));
            result.setResponseCode(ResponseCode.RESPONSE_OK);
        }
        return result;
    }
}
