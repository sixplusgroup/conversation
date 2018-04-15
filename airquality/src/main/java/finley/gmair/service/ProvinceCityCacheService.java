package finley.gmair.service;

import finley.gmair.util.ResultData;

public interface ProvinceCityCacheService {

    ResultData fetch(String cityName);

    ResultData fetchAll();
}
