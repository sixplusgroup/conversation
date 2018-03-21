package finley.gmair.service;

import finley.gmair.model.district.City;
import finley.gmair.model.district.District;
import finley.gmair.model.district.Province;
import finley.gmair.util.ResultData;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Map;

public interface DistrictDivisionService {
    @Transactional
    ResultData createProvince(Province province);
    @Transactional
    ResultData createProvince(List<Province> provinces);
    ResultData fetchProvince(Map<String, Object> condition);
    ResultData modifyProvince(Province province);

    @Transactional
    ResultData createCity(City city);
    @Transactional
    ResultData createCity(List<City> cities);
    ResultData fetchCity(Map<String, Object> condition);
    ResultData modifyCity(City city);

    @Transactional
    ResultData createDistrict(District district);
    @Transactional
    ResultData createDistrict(List<District> districts);
    ResultData fetchDistrict(Map<String, Object> condition);
    ResultData modifyDistrict(District district);
}
