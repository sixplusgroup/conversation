package finley.gmair.service;

import finley.gmair.model.district.City;
import finley.gmair.util.ResultData;

import java.util.Map;

public interface ObscureCityCacheService {

    ResultData generate(String cityName, String cityId);

    ResultData generate(Map<String, String> map);

    ResultData fetch(String cityName);

}
