package finley.gmair.dao;

import finley.gmair.model.district.City;
import finley.gmair.model.district.District;
import finley.gmair.model.district.Province;
import finley.gmair.util.ResultData;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Map;

public interface DistrictDivisionDao {
    @Transactional
    ResultData insertProvince(Province province);
    @Transactional
    ResultData insertProvince(List<Province> provinces);
    ResultData queryProvince(Map<String, Object> condition);
    ResultData updateProvince(Province province);

    @Transactional
    ResultData insertCity(City city);
    @Transactional
    ResultData insertCity(List<City> cities);
    ResultData queryCity(Map<String, Object> condition);
    ResultData updateCity(City city);

    @Transactional
    ResultData insertDistrict(District district);
    @Transactional
    ResultData insertDistrict(List<District> districts);
    ResultData queryDistrict(Map<String, Object> condition);
    ResultData updateDistrict(District district);
}
