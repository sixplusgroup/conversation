package finley.gmair.dao.impl;

import finley.gmair.dao.BaseDao;
import finley.gmair.dao.ProvinceDao;
import finley.gmair.model.district.Province;
import finley.gmair.util.ResponseCode;
import finley.gmair.util.ResultData;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Map;

@Repository
public class ProvinceDaoImpl extends BaseDao implements ProvinceDao {

    @Override
    public ResultData insertProvince(Province province) {
        ResultData result = new ResultData();
        try {
            sqlSession.insert("gmair.location.province.insert", province);
            result.setData(province);
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
            List<Province> list = sqlSession.selectList("gmair.location.province.query", condition);
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
        return null;
    }
}
