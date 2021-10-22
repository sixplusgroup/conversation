package finley.gmair.dao.impl;

import com.alibaba.fastjson.JSON;
import finley.gmair.dao.BaseDao;
import finley.gmair.dao.DistrictDao;
import finley.gmair.model.district.District;
import finley.gmair.util.ResponseCode;
import finley.gmair.util.ResultData;
import finley.gmair.vo.location.DistrictCityVo;
import org.springframework.stereotype.Repository;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Repository
public class DistrictDaoImpl extends BaseDao implements DistrictDao {

    @Override
    public ResultData insertDistrict(District district, String cityId) {
        ResultData result = new ResultData();
        Map<String, Object> value = new HashMap<>();
        value.put("district", district);
        value.put("cityId", cityId);
        try {
            sqlSession.insert("gmair.location.district.insert", value);
            result.setData(district);
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
            List<District> list = sqlSession.selectList("gmair.location.district.query", condition);
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

    public ResultData queryDistrictCityVo(Map<String, Object> condition) {
        ResultData result = new ResultData();
        try {
            List<DistrictCityVo> list = sqlSession.selectList("gmair.location.district.queryCity", condition);
            System.out.println(JSON.toJSONString(list));
            if (list.isEmpty()) {
                result.setResponseCode(ResponseCode.RESPONSE_NULL);
            }
            result.setData(list);
        } catch (Exception e) {
            System.out.println(e.getMessage());
            result.setResponseCode(ResponseCode.RESPONSE_ERROR);
            result.setDescription(e.getMessage());
        }
        return result;
    }

    @Override
    public ResultData updateDistrict(District district) {
        ResultData result = new ResultData();
        try {
            sqlSession.update("gmair.location.district.update", district);
            result.setData(result);
        } catch (Exception e) {
            result.setResponseCode(ResponseCode.RESPONSE_ERROR);
            result.setDescription(e.getMessage());
        }
        return result;
    }
}
