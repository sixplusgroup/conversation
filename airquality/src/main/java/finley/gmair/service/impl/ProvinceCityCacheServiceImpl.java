package finley.gmair.service.impl;

import finley.gmair.service.ProvinceCityCacheService;
import finley.gmair.service.feign.LocationFeign;
import finley.gmair.util.ResponseCode;
import finley.gmair.util.ResultData;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.annotation.PostConstruct;
import java.util.*;


@Service
public class ProvinceCityCacheServiceImpl implements ProvinceCityCacheService{
    private Map<String, String> provinceCityMap = new HashMap<>();
    private Map<String, String> city2provinceMap = new HashMap<>();
    private Map<String, String> provinceId2NameMap = new HashMap<>();
    private Map<String, String> cityId2NameMap = new HashMap<>();
    private Set<String> citySet = new HashSet<>();

    @Autowired
    LocationFeign locationFeign;

    @PostConstruct
    public void init() {
        try {
            ResultData response = locationFeign.province();
            List<LinkedHashMap> provinceList = (List<LinkedHashMap>) response.getData();
            for (int i = 0; i < provinceList.size(); i++) {
                String provinceId = (String) provinceList.get(i).get("provinceId");
                String provinceName = (String) provinceList.get(i).get("provinceName");
                provinceId2NameMap.put(provinceId, provinceName);
                response = locationFeign.city(provinceId);
                List<LinkedHashMap> cityList = (List<LinkedHashMap>) response.getData();
                for (LinkedHashMap city : cityList) {
                    provinceCityMap.put((String) city.get("cityName"), (String) city.get("cityId"));
                    city2provinceMap.put((String) city.get("cityId"), provinceId);
                    cityId2NameMap.put((String) city.get("cityId"), (String) city.get("cityName"));
                    citySet.add((String) city.get("cityId"));
                }
                city2provinceMap.put(provinceId, provinceId);
                provinceCityMap.put(provinceName, provinceId);
                cityId2NameMap.put(provinceId, provinceName);
                citySet.add(provinceId);
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
        String province = city2provinceMap.get(cityId);
        if (province == null) {
            System.out.println(cityId);
        }
        return province;
    }

    @Override
    public String fetchProvinceName(String provinceId) {
        return provinceId2NameMap.get(provinceId);
    }

    @Override
    public String fetchCityName(String cityId) {
        return cityId2NameMap.get(cityId);
    }

    @Override
    public Set<String> getAvailableCity() {
        return citySet;
    }
}
