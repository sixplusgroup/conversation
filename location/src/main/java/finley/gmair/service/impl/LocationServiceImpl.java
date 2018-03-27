package finley.gmair.service.impl;

import finley.gmair.dao.CityDao;
import finley.gmair.dao.DistrictDao;
import finley.gmair.dao.ProvinceDao;
import finley.gmair.model.district.City;
import finley.gmair.model.district.District;
import finley.gmair.model.district.Province;
import finley.gmair.service.LocationService;
import finley.gmair.util.ResponseCode;
import finley.gmair.util.ResultData;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.Map;

@Service
public class LocationServiceImpl implements LocationService {
    private Logger logger = LoggerFactory.getLogger(LocationServiceImpl.class);

    @Autowired
    private ProvinceDao provinceDao;

    @Autowired
    private CityDao cityDao;

    @Autowired
    private DistrictDao districtDao;

    @Override
    public ResultData createProvince(Province province) {
        ResultData result = new ResultData();
        Map<String, Object> condition = new HashMap<>();
        condition.put("provinceId", province.getProvinceId());
        ResultData response = provinceDao.queryProvince(condition);
        if (response.getResponseCode() == ResponseCode.RESPONSE_OK) {
            result.setResponseCode(ResponseCode.RESPONSE_OK);
            result.setDescription("Province already exist");
            return result;
        }
        if (response.getResponseCode() == ResponseCode.RESPONSE_ERROR) {
            result.setResponseCode(ResponseCode.RESPONSE_ERROR);
            result.setDescription("Fail to query province information from database");
            return result;
        }
        response = provinceDao.insertProvince(province);
        if (response.getResponseCode() == ResponseCode.RESPONSE_OK) {
            result.setResponseCode(ResponseCode.RESPONSE_OK);
            result.setData(response.getData());
            return result;
        }
        result.setResponseCode(ResponseCode.RESPONSE_ERROR);
        result.setDescription("Fail to store province to database");
        return result;
    }

    @Override
    public ResultData queryProvince(Map<String, Object> condition) {
        ResultData result = new ResultData();
        ResultData response = provinceDao.queryProvince(condition);
        if (response.getResponseCode() == ResponseCode.RESPONSE_OK) {
            result.setResponseCode(ResponseCode.RESPONSE_OK);
            result.setData(response.getData());
        }
        if (result.getResponseCode() == ResponseCode.RESPONSE_NULL) {
            result.setResponseCode(ResponseCode.RESPONSE_NULL);
            result.setDescription("No province found from database");
        }
        if (result.getResponseCode() == ResponseCode.RESPONSE_ERROR) {
            result.setResponseCode(ResponseCode.RESPONSE_ERROR);
            result.setDescription("Fail to query province information from database");
        }
        return result;
    }

    @Override
    public ResultData createCity(City city, String provinceId) {
        ResultData result = new ResultData();
        Map<String, Object> condition = new HashMap<>();
        condition.put("cityId", city.getCityId());
        ResultData response = cityDao.queryCity(condition);
        if (response.getResponseCode() == ResponseCode.RESPONSE_OK) {
            result.setResponseCode(ResponseCode.RESPONSE_OK);
            result.setDescription("City already exist");
            return result;
        }
        if (response.getResponseCode() == ResponseCode.RESPONSE_ERROR) {
            result.setResponseCode(ResponseCode.RESPONSE_ERROR);
            result.setDescription("Fail to query city information from database");
            return result;
        }
        response = cityDao.insertCity(city, provinceId);
        if (response.getResponseCode() == ResponseCode.RESPONSE_OK) {
            result.setResponseCode(ResponseCode.RESPONSE_OK);
            result.setData(response.getData());
            return result;
        }
        result.setResponseCode(ResponseCode.RESPONSE_ERROR);
        result.setDescription("Fail to store city to database");
        return result;
    }

    @Override
    public ResultData createDistrict(District district, String cityId) {
        ResultData result = new ResultData();
        ResultData response = districtDao.insertDistrict(district, cityId);
        if (response.getResponseCode() == ResponseCode.RESPONSE_OK) {
            result.setResponseCode(ResponseCode.RESPONSE_OK);
            result.setData(response.getData());
            return result;
        }
        result.setResponseCode(ResponseCode.RESPONSE_ERROR);
        result.setDescription("Fail to store district to database");
        return result;
    }
}
