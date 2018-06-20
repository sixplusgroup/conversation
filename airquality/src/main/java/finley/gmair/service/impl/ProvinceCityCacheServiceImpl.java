package finley.gmair.service.impl;

import finley.gmair.service.ProvinceCityCacheService;
import finley.gmair.service.feign.LocationFeign;
import finley.gmair.util.ResponseCode;
import finley.gmair.util.ResultData;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.annotation.PostConstruct;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;


@Service
public class ProvinceCityCacheServiceImpl implements ProvinceCityCacheService{
    private Map<String, String> provinceCityMap = new HashMap<>();
    private Map<String, String> city2provinceMap = new HashMap<>();

    @Autowired
    LocationFeign locationFeign;

    @PostConstruct
    public void init() {
        try {
            ResultData response = locationFeign.province();
            List<LinkedHashMap> provinceList = (List<LinkedHashMap>) response.getData();
            for (int i = 0; i < provinceList.size(); i++) {
                String provinceId = (String) provinceList.get(i).get("provinceId");
                response = locationFeign.city(provinceId);
                List<LinkedHashMap> cityList = (List<LinkedHashMap>) response.getData();
                for (LinkedHashMap city : cityList) {
                    provinceCityMap.put((String) city.get("cityName"), (String) city.get("cityId"));
                    city2provinceMap.put((String) city.get("cityId"), provinceId);
                }
                city2provinceMap.put(provinceId, provinceId);
                provinceCityMap.put((String) provinceList.get(i).get("provinceName"),
                        (String) provinceList.get(i).get("provinceId"));
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    @Override
    public ResultData fetch(String cityName) {
        ResultData result = new ResultData();
        if (provinceCityMap.get(cityName) == null) {
            result.setResponseCode(ResponseCode.RESPONSE_NULL);
        } else {
            result.setData(provinceCityMap.get(cityName));
        }
        return result;
    }

    @Override
    public ResultData fetchAll() {
        ResultData result = new ResultData();
        result.setData(provinceCityMap);
        return result;
    }

    @Override
    public String fetchProvince(String cityId) {
        return city2provinceMap.get(cityId);
    }
}
