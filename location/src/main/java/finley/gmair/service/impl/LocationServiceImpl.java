package finley.gmair.service.impl;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
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
    public ResultData fetchProvince(Map<String, Object> condition) {
        ResultData result = new ResultData();
        ResultData response = provinceDao.queryProvince(condition);
        if (response.getResponseCode() == ResponseCode.RESPONSE_OK) {
            result.setResponseCode(ResponseCode.RESPONSE_OK);
            result.setData(response.getData());
        }
        if (response.getResponseCode() == ResponseCode.RESPONSE_NULL) {
            result.setResponseCode(ResponseCode.RESPONSE_NULL);
            result.setDescription("No province found from database");
        }
        if (response.getResponseCode() == ResponseCode.RESPONSE_ERROR) {
            result.setResponseCode(ResponseCode.RESPONSE_ERROR);
            result.setDescription("Fail to retrieve province from database");
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
    public ResultData fetchCity(Map<String, Object> condition) {
        ResultData result = new ResultData();
        ResultData response = cityDao.queryCity(condition);
        if (response.getResponseCode() == ResponseCode.RESPONSE_OK) {
            result.setResponseCode(ResponseCode.RESPONSE_OK);
            result.setData(response.getData());
        }
        if (response.getResponseCode() == ResponseCode.RESPONSE_NULL) {
            result.setResponseCode(ResponseCode.RESPONSE_NULL);
            result.setDescription("No city found from database");
        }
        if (response.getResponseCode() == ResponseCode.RESPONSE_ERROR) {
            result.setResponseCode(ResponseCode.RESPONSE_ERROR);
            result.setDescription("Fail to retrieve city from database");
        }
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

    @Override
    public ResultData fetchDistrict(Map<String, Object> condition) {
        ResultData result = new ResultData();
        ResultData response = districtDao.queryDistrict(condition);
        if (response.getResponseCode() == ResponseCode.RESPONSE_OK) {
            result.setResponseCode(ResponseCode.RESPONSE_OK);
            result.setData(response.getData());
        }
        if (response.getResponseCode() == ResponseCode.RESPONSE_NULL) {
            result.setResponseCode(ResponseCode.RESPONSE_NULL);
            result.setDescription("No district found from database");
        }
        if (response.getResponseCode() == ResponseCode.RESPONSE_ERROR) {
            result.setResponseCode(ResponseCode.RESPONSE_ERROR);
            result.setDescription("Fail to retrieve district information");
        }
        return result;
    }

    @Override
    public void process(JSONObject response) {
        JSONArray data = response.getJSONArray("result");
        JSONArray provinces = data.getJSONArray(0);
        JSONArray cities = data.getJSONArray(1);
        JSONArray districts = data.getJSONArray(2);
        JSONObject province, city, district;
        boolean flag = false;
        int i, j, k;
        for (i = 0, j = 0, k = 0; k < districts.size(); k++) {
            //assign the province, city and district
            province = provinces.getJSONObject(i);
            city = cities.getJSONObject(j);
            district = districts.getJSONObject(k);
            if (i == 0 && j == 0 && k == 0) {
                createProvince(new Province(province));
                createCity(new City(city), province.getString("id"));
            }
            int pEnd = province.getJSONArray("cidx").getIntValue(1);
            //judge whether the province is a province-level municipality
            if (!city.containsKey("cidx")) {
                flag = true;
                if (j < pEnd + 1) {
                    if (j != 0) createCity(new City(city), province.getString("id"));
                    j++;
                    k--;
                    continue;
                }
                province = provinces.getJSONObject(++i);
                //move to the next province
                createProvince(new Province(province));
                k--;
                continue;
            }
            if (flag) {
                if (j >= pEnd + 1) {
                    province = provinces.getJSONObject(++i);
                    createProvince(new Province(province));
                }
                createCity(new City(city), province.getString("id"));
                flag = false;
            }
            //judge whether the city exist in city list
            int cEnd = city.getJSONArray("cidx").getIntValue(1);
            //if out of city range but still in province range, read the next city
            if (k < cEnd + 1) {
                //the district still belongs to the current city
                createDistrict(new District(district), city.getString("id"));
                continue;
            }
            city = cities.getJSONObject(++j);
            if (j < pEnd + 1) {
                //the city still belongs to the current province
                createCity(new City(city), province.getString("id"));
                k--;
                continue;
            }
            province = provinces.getJSONObject(++i);
            //move to the next province
            createProvince(new Province(province));
            createCity(new City(city), province.getString("id"));
            k--;
        }
        for (; j < cities.size(); j++) {
            province = provinces.getJSONObject(i);
            city = cities.getJSONObject(j);
            int pEnd = province.getJSONArray("cidx").getIntValue(1);
            if (j < pEnd + 1) {
                createCity(new City(city), province.getString("id"));
                continue;
            }
            province = provinces.getJSONObject(++i);
            j--;
            //move to the next province
            createProvince(new Province(province));
        }
        for (; i < provinces.size(); i++) {
            province = provinces.getJSONObject(i);
            createProvince(new Province(province));
        }
    }
}
