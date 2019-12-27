package finley.gmair.dao.impl;

import finley.gmair.dao.BaseDao;
import finley.gmair.dao.CityDao;
import finley.gmair.model.district.City;
import finley.gmair.util.ResponseCode;
import finley.gmair.util.ResultData;
import finley.gmair.vo.location.CityProvinceVo;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Repository;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Repository
public class CityDaoImpl extends BaseDao implements CityDao {
    private Logger logger = LoggerFactory.getLogger(CityDaoImpl.class);

    @Override
    public ResultData insertCity(City city, String provinceId) {
        ResultData result = new ResultData();
        Map<String, Object> value = new HashMap<>();
        value.put("city", city);
        value.put("provinceId", provinceId);
        try {
            sqlSession.insert("gmair.location.city.insert", value);
            result.setData(city);
        } catch (Exception e) {
            logger.error(e.getMessage());
            result.setResponseCode(ResponseCode.RESPONSE_ERROR);
            result.setDescription(e.getMessage());
        }
        return result;
    }

    @Override
    public ResultData queryCity(Map<String, Object> condition) {
        ResultData result = new ResultData();
        try {
            List<City> list = sqlSession.selectList("gmair.location.city.query", condition);
            if (list.isEmpty()) {
                result.setResponseCode(ResponseCode.RESPONSE_NULL);
            }
            result.setData(list);
        } catch (Exception e) {
            logger.error(e.getMessage());
            result.setResponseCode(ResponseCode.RESPONSE_ERROR);
            result.setDescription(e.getMessage());
        }
        return result;
    }

    @Override
    public ResultData queryProvinceIdByCityId(Map<String, Object> condition) {
        ResultData result = new ResultData();
        try {
            List<CityProvinceVo> list = sqlSession.selectList("gmair.location.city.queryProvinceId", condition);
            if (list.isEmpty()) {
                result.setResponseCode(ResponseCode.RESPONSE_NULL);
            }
            result.setData(list);
        } catch (Exception e) {
            logger.error(e.getMessage());
            result.setResponseCode(ResponseCode.RESPONSE_ERROR);
            result.setDescription(e.getMessage());
        }
        return result;
    }

    @Override
    public ResultData updateCity(City city) {
        ResultData result = new ResultData();
        try {
            sqlSession.update("gmair.location.city.update", city);
            result.setData(city);
        } catch (Exception e) {
            logger.error(e.getMessage());
            result.setResponseCode(ResponseCode.RESPONSE_ERROR);
            result.setDescription(e.getMessage());
        }
        return result;
    }
}
