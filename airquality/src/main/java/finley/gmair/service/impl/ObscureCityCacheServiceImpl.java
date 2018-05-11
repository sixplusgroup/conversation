package finley.gmair.service.impl;

import finley.gmair.model.air.ObscureCity;
import finley.gmair.service.ObscureCityCacheService;
import finley.gmair.service.ObscureCityService;
import finley.gmair.util.ResponseCode;
import finley.gmair.util.ResultData;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;

import javax.annotation.PostConstruct;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
public class ObscureCityCacheServiceImpl implements ObscureCityCacheService {

    @Autowired
    private ObscureCityService obscureCityService;

    private Map<String, String> cityMap = new HashMap<>();

    @PostConstruct
    private void init() {
        Map<String, Object> condition = new HashMap<>();
        condition.put("blockFlag", false);
        ResultData response = obscureCityService.fetch(condition);
        if (response.getResponseCode() == ResponseCode.RESPONSE_OK) {
            List<ObscureCity> list = (List<ObscureCity>) response.getData();
            list.forEach(e -> cityMap.put(e.getCityName(), e.getCityId()));
        }
    }

    @Cacheable("cityMap")
    @Override
    public ResultData generate(String cityName, String cityId) {
        ResultData result = new ResultData();
        cityMap.put(cityName, cityId);
        ObscureCity obscureCity = new ObscureCity();
        obscureCity.setCityId(cityId);
        obscureCity.setCityName(cityName);
        obscureCityService.assign(obscureCity);
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
