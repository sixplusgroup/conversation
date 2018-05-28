package finley.gmair.service.impl;

import com.alibaba.fastjson.JSONObject;
import finley.gmair.model.air.ObscureCity;
import finley.gmair.service.ObscureCityCacheService;
import finley.gmair.service.ObscureCityService;
import finley.gmair.service.ProvinceCityCacheService;
import finley.gmair.service.feign.LocationFeign;
import finley.gmair.util.ResponseCode;
import finley.gmair.util.ResultData;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

@Service
public class ObscureCityCacheServiceImpl implements ObscureCityCacheService {

    @Autowired
    private ObscureCityService obscureCityService;

    @Autowired
    private LocationFeign locationFeign;

    @Autowired
    private ProvinceCityCacheService provinceCityCacheService;

    @Cacheable(value = "ObscureCityMap", key = "#cityName", unless = "#result == null")
    @Override
    public ObscureCity fetch(String cityName) {
        ObscureCity obscureCity = null;
        Map<String, Object> condition = new HashMap<>();
        condition.put("cityName", cityName);
        condition.put("blockFlag", false);
        ResultData response = obscureCityService.fetch(condition);
        if (response.getResponseCode() == ResponseCode.RESPONSE_OK) {
            obscureCity = ((List<ObscureCity>) response.getData()).get(0);
            return obscureCity;
        }
        response = locationFeign.geocoder(cityName);
        if (response.getResponseCode() == ResponseCode.RESPONSE_OK) {
            JSONObject jsonObject = new JSONObject((LinkedHashMap<String, Object>) response.getData());
            if (!StringUtils.isEmpty(jsonObject.get("address_components"))) {
                String accurateCity = jsonObject.getJSONObject("address_components").getString("city");
                response = provinceCityCacheService.fetch(accurateCity);
                if (response.getResponseCode() == ResponseCode.RESPONSE_OK) {
                    String cityId = (String) response.getData();
                    obscureCity = new ObscureCity(cityName, cityId);
                    obscureCityService.assign(obscureCity);
                } else {
                    System.out.println(accurateCity + " has been processed");
                }
            }
        } else {
            System.out.println(response.getDescription() + cityName);
        }
        try {
            Thread.sleep(500);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return obscureCity;
    }
}
