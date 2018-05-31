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

    @Autowired
    LocationFeign locationFeign;

    @PostConstruct
    public void init() {
        ResultData response = locationFeign.province();
        List<LinkedHashMap> provinceList = (List<LinkedHashMap>) response.getData();
        for (int i = 0; i < provinceList.size(); i++) {
            response = locationFeign.city((String) provinceList.get(i).get("provinceId"));
            List<LinkedHashMap> cityList = (List<LinkedHashMap>) response.getData();
            for (LinkedHashMap city : cityList) {
                provinceCityMap.put((String) city.get("cityName"), (String) city.get("cityId"));
            }
            provinceCityMap.put((String) provinceList.get(i).get("provinceName"),
                    (String) provinceList.get(i).get("provinceId"));
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
}
