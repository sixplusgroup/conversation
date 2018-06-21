package finley.gmair.service;

import finley.gmair.util.ResultData;

public interface ProvinceCityCacheService {

    ResultData fetch(String cityName);

    ResultData fetchAll();

    String fetchProvince(String cityId);

    String fetchProvinceName(String provinceId);

    String fetchCityName(String cityId);
}
