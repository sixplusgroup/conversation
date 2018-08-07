package finley.gmair.dao;

import finley.gmair.model.district.City;
import finley.gmair.util.ResultData;

import java.util.Map;

public interface CityDao {
    ResultData insertCity(City city, String provinceId);

    ResultData queryCity(Map<String, Object> condition);

    ResultData queryProvinceIdByCityId(Map<String, Object> condition);

    ResultData updateCity(City city);
}
