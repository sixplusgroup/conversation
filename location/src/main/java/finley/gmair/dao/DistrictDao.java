package finley.gmair.dao;

import finley.gmair.model.district.District;
import finley.gmair.util.ResultData;

import java.util.Map;

public interface DistrictDao {
    ResultData insertDistrict(District district, String cityId);

    ResultData queryDistrict(Map<String, Object> condition);

    ResultData updateDistrict(District district);

    ResultData queryDistrictCityVo(Map<String, Object> condition);
}
