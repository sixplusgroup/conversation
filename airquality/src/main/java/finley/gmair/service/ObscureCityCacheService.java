package finley.gmair.service;

import finley.gmair.model.air.ObscureCity;
import finley.gmair.model.district.City;
import finley.gmair.util.ResultData;
import org.springframework.cache.annotation.CachePut;

import java.util.Map;

public interface ObscureCityCacheService {


    @CachePut(value = "ObscureCityMap", key = "#obscureCity.cityName", condition = "#obscureCity != null")
    default void generate(ObscureCity obscureCity){};

    ObscureCity fetch(String cityName);

}
