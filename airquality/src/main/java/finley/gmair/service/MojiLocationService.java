package finley.gmair.service;

import finley.gmair.model.air.MojiRecord;
import finley.gmair.model.district.City;
import finley.gmair.model.district.District;
import finley.gmair.model.district.Province;

import java.util.Map;

public interface MojiLocationService {
    String fetch(int cityId);

    String fetch(double longitude, double latitude);

    MojiRecord locate(String district);

    Map<String, Province> getProvinces();

    Map<String, City> getCities();

    Map<String, District> getDistricts();
}
