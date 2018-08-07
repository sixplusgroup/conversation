package finley.gmair.service;

import com.alibaba.fastjson.JSONObject;
import finley.gmair.model.district.City;
import finley.gmair.model.district.District;
import finley.gmair.model.district.Province;
import finley.gmair.util.ResultData;

import java.util.Map;

public interface LocationService {
    ResultData createProvince(Province province);

    ResultData fetchProvince(Map<String, Object> condition);

    ResultData createCity(City city, String provinceId);

    ResultData fetchCity(Map<String, Object> condition);

    ResultData createDistrict(District district, String cityId);

    ResultData fetchDistrict(Map<String, Object> condition);

    ResultData fetchProvinceIdByCityId(Map<String, Object> condition);

    void process(JSONObject response);
}
