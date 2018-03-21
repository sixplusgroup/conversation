package finley.gmair.dao.impl;

import finley.gmair.dao.BaseDao;
import finley.gmair.dao.DistrictDivisionDao;
import finley.gmair.model.district.City;
import finley.gmair.model.district.District;
import finley.gmair.model.district.Province;
import finley.gmair.util.ResponseCode;
import finley.gmair.util.ResultData;
import finley.gmair.vo.districtdivision.CityVo;
import finley.gmair.vo.districtdivision.DistrictVo;
import finley.gmair.vo.districtdivision.ProvinceVo;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Map;

@Repository
public class DistrictDivisionDaoImpl extends BaseDao implements DistrictDivisionDao {
    @Override
    @Transactional
    public ResultData insertProvince(Province province) {
        ResultData result = new ResultData();
        try {
            sqlSession.insert("gmair.districtdivision.province.insert", province);
            result.setData(province);
        } catch (Exception e) {
            result.setResponseCode(ResponseCode.RESPONSE_ERROR);
            result.setDescription(e.getMessage());
        }
        return result;
    }

    @Override
    @Transactional
    public ResultData insertProvince(List<Province> provinces) {
        ResultData result = new ResultData();
        try {
            sqlSession.insert("gmair.districtdivision.province.insertBatch", provinces);
            result.setData(provinces);
        } catch (Exception e) {
            result.setResponseCode(ResponseCode.RESPONSE_ERROR);
            result.setDescription(e.getMessage());
        }
        return result;
    }

    @Override
    public ResultData queryProvince(Map<String, Object> condition) {
        ResultData result = new ResultData();
        try {
            List<ProvinceVo> list = sqlSession.selectList("gmair.districtdivision.province.query", condition);
            if (list.isEmpty()) {
                result.setResponseCode(ResponseCode.RESPONSE_NULL);
            }
            result.setData(list);
        } catch (Exception e) {
            result.setResponseCode(ResponseCode.RESPONSE_ERROR);
            result.setDescription(e.getMessage());
        }
        return result;
    }

    @Override
    public ResultData updateProvince(Province province) {
        ResultData result = new ResultData();
        try {
            sqlSession.update("gmair.districtdivision.province.update", province);
            result.setData(province);
        } catch (Exception e) {
            result.setResponseCode(ResponseCode.RESPONSE_ERROR);
            result.setDescription(e.getMessage());
        }
        return result;
    }

    @Override
    @Transactional
    public ResultData insertCity(City city) {
        ResultData result = new ResultData();
        try {
            sqlSession.insert("gmair.districtdivision.city.insert", city);
            result.setData(city);
        } catch (Exception e) {
            result.setResponseCode(ResponseCode.RESPONSE_ERROR);
            result.setDescription(e.getMessage());
        }
        return result;
    }

    @Override
    @Transactional
    public ResultData insertCity(List<City> cities) {
        ResultData result = new ResultData();
        try {
            sqlSession.insert("gmair.districtdivision.city.insertBatch", cities);
            result.setData(cities);
        } catch (Exception e) {
            result.setResponseCode(ResponseCode.RESPONSE_ERROR);
            result.setDescription(e.getMessage());
        }
        return result;
    }

    @Override
    public ResultData queryCity(Map<String, Object> condition) {
        ResultData result = new ResultData();
        try {
            List<CityVo> list = sqlSession.selectList("gmair.districtdivision.city.query", condition);
            if (list.isEmpty()) {
                result.setResponseCode(ResponseCode.RESPONSE_NULL);
            }
            result.setData(list);
        } catch (Exception e) {
            result.setResponseCode(ResponseCode.RESPONSE_ERROR);
            result.setDescription(e.getMessage());
        }
        return result;
    }

    @Override
    public ResultData updateCity(City city) {
        ResultData result = new ResultData();
        try {
            sqlSession.update("gmair.districtdivision.city.update", city);
            result.setData(city);
        } catch (Exception e) {
            result.setResponseCode(ResponseCode.RESPONSE_ERROR);
            result.setDescription(e.getMessage());
        }
        return result;
    }

    @Override
    @Transactional
    public ResultData insertDistrict(District district) {
        ResultData result = new ResultData();
        try {
            sqlSession.insert("gmair.districtdivision.district.insert", district);
            result.setData(district);
        } catch (Exception e) {
            result.setResponseCode(ResponseCode.RESPONSE_ERROR);
            result.setDescription(e.getMessage());
        }
        return result;
    }

    @Override
    @Transactional
    public ResultData insertDistrict(List<District> districts) {
        ResultData result = new ResultData();
        try {
            sqlSession.insert("gmair.districtdivision.district.insertBatch", districts);
            result.setData(districts);
        } catch (Exception e) {
            result.setResponseCode(ResponseCode.RESPONSE_ERROR);
            result.setDescription(e.getMessage());
        }
        return result;
    }

    @Override
    public ResultData queryDistrict(Map<String, Object> condition) {
        ResultData result = new ResultData();
        try {
            List<DistrictVo> list = sqlSession.selectList("gmair.districtdivision.district.query", condition);
            if (list.isEmpty()) {
                result.setResponseCode(ResponseCode.RESPONSE_NULL);
            }
            result.setData(list);
        } catch (Exception e) {
            result.setResponseCode(ResponseCode.RESPONSE_ERROR);
            result.setDescription(e.getMessage());
        }
        return result;
    }

    @Override
    public ResultData updateDistrict(District district) {
        ResultData result = new ResultData();
        try {
            sqlSession.update("gmair.districtdivision.district.update", district);
            result.setData(district);
        } catch (Exception e) {
            result.setResponseCode(ResponseCode.RESPONSE_ERROR);
            result.setDescription(e.getMessage());
        }
        return result;
    }
}