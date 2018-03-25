package finley.gmair.service;

import finley.gmair.model.district.City;
import finley.gmair.model.district.District;
import finley.gmair.model.district.Province;
import finley.gmair.util.ResultData;

import java.util.Map;

public interface LocationService {
    ResultData createProvince(Province province);

    ResultData queryProvince(Map<String, Object> condition);

    ResultData createCity(City city, String provinceId);

    ResultData createDistrict(District district, String cityId);
}
