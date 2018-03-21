package finley.gmair.service.impl;

import finley.gmair.dao.DistrictDivisionDao;
import finley.gmair.model.district.City;
import finley.gmair.model.district.District;
import finley.gmair.model.district.Province;
import finley.gmair.service.DistrictDivisionService;
import finley.gmair.util.ResponseCode;
import finley.gmair.util.ResultData;
import finley.gmair.vo.districtdivision.CityVo;
import finley.gmair.vo.districtdivision.DistrictVo;
import finley.gmair.vo.districtdivision.ProvinceVo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Map;

@Service
public class DistrictDivisionServiceImpl implements DistrictDivisionService {

    @Autowired
    private DistrictDivisionDao districtDivisionDao;

    @Override
    @Transactional
    public ResultData createProvince(Province province) {
        ResultData result = new ResultData();
        ResultData response = districtDivisionDao.insertProvince(province);
        result.setResponseCode(response.getResponseCode());
        if (response.getResponseCode() == ResponseCode.RESPONSE_OK) {
            result.setData(response.getData());
        } else {
            result.setDescription(response.getDescription());
        }
        return result;
    }

    @Override
    public ResultData createProvince(List<Province> provinces) {
        ResultData result = new ResultData();
        ResultData response = districtDivisionDao.insertProvince(provinces);
        result.setResponseCode(response.getResponseCode());
        if (response.getResponseCode() == ResponseCode.RESPONSE_OK) {
            result.setData(response.getData());
        } else {
            result.setDescription(response.getDescription());
        }
        return result;
    }

    @Override
    public ResultData fetchProvince(Map<String, Object> condition) {
        ResultData result = new ResultData();
        ResultData response = districtDivisionDao.queryProvince(condition);
        result.setResponseCode(response.getResponseCode());
        if (response.getResponseCode() != ResponseCode.RESPONSE_OK) {
            return response;
        } else {
            List<ProvinceVo> list = (List<ProvinceVo>) response.getData();
            result.setData(list);
            return result;
        }
    }

    @Override
    public ResultData modifyProvince(Province province) {
        return districtDivisionDao.updateProvince(province);
    }

    @Override
    @Transactional
    public ResultData createCity(City city) {
        ResultData result = new ResultData();
        ResultData response = districtDivisionDao.insertCity(city);
        result.setResponseCode(response.getResponseCode());
        if (response.getResponseCode() == ResponseCode.RESPONSE_OK) {
            result.setData(response.getData());
        } else {
            result.setDescription(response.getDescription());
        }
        return result;
    }

    @Override
    @Transactional
    public ResultData createCity(List<City> cities) {
        ResultData result = new ResultData();
        ResultData response = districtDivisionDao.insertCity(cities);
        if (response.getResponseCode() == ResponseCode.RESPONSE_OK) {
            result.setData(response.getData());
        } else {
            result.setDescription(response.getDescription());
        }
        return result;
    }

    @Override
    public ResultData fetchCity(Map<String, Object> condition) {
        ResultData result = new ResultData();
        ResultData response = districtDivisionDao.queryCity(condition);
        result.setResponseCode(response.getResponseCode());
        if (response.getResponseCode() != ResponseCode.RESPONSE_OK) {
            return response;
        } else {
            List<CityVo> list = (List<CityVo>) response.getData();
            result.setData(list);
            return result;
        }
    }

    @Override
    public ResultData modifyCity(City city) {
        return districtDivisionDao.updateCity(city);
    }

    @Override
    @Transactional
    public ResultData createDistrict(District district) {
        ResultData result = new ResultData();
        ResultData response = districtDivisionDao.insertDistrict(district);
        result.setResponseCode(response.getResponseCode());
        if (response.getResponseCode() == ResponseCode.RESPONSE_OK) {
            result.setData(response.getData());
        } else {
            result.setDescription(response.getDescription());
        }
        return result;
    }

    @Override
    @Transactional
    public ResultData createDistrict(List<District> districts) {
        ResultData result = new ResultData();
        ResultData response = districtDivisionDao.insertDistrict(districts);
        result.setResponseCode(response.getResponseCode());
        if (response.getResponseCode() == ResponseCode.RESPONSE_OK) {
            result.setData(response.getData());
        } else {
            result.setDescription(response.getDescription());
        }
        return result;
    }

    @Override
    public ResultData fetchDistrict(Map<String, Object> condition) {
        ResultData result = new ResultData();
        ResultData response = districtDivisionDao.queryDistrict(condition);
        result.setResponseCode(response.getResponseCode());
        if (response.getResponseCode() != ResponseCode.RESPONSE_OK) {
            return response;
        } else {
            List<DistrictVo> list = (List<DistrictVo>) response.getData();
            result.setData(list);
            return result;
        }
    }

    @Override
    public ResultData modifyDistrict(District district) {
        return districtDivisionDao.updateDistrict(district);
    }
}