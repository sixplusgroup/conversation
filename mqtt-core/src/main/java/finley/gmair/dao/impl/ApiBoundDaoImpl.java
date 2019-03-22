package finley.gmair.dao.impl;

import finley.gmair.dao.ApiBoundDao;
import finley.gmair.dao.BaseDao;
import finley.gmair.model.mqtt.ApiBound;
import finley.gmair.util.IDGenerator;
import finley.gmair.util.ResponseCode;
import finley.gmair.util.ResultData;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Map;

@Repository
public class ApiBoundDaoImpl extends BaseDao implements ApiBoundDao {
    @Override
    public ResultData insertApiBound(ApiBound bound) {
        ResultData result = new ResultData();
        bound.setBoundId(IDGenerator.generate("ABI"));
        try {
            sqlSession.insert("gmair.mqtt.apibound.insert", bound);
            result.setData(bound);
        } catch (Exception e) {
            result.setResponseCode(ResponseCode.RESPONSE_ERROR);
            result.setDescription(e.getMessage());
        }
        return result;
    }

    @Override
    public ResultData queryApiBound(Map<String, Object> condition) {
        ResultData result = new ResultData();
        try {
            List<ApiBound> list = sqlSession.selectList("gmair.mqtt.apibound.query", condition);
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
    public ResultData updateApiBound(Map<String, Object> condition) {
        ResultData result = new ResultData();
        try {
            sqlSession.update("gmair.mqtt.apibound.update", condition);
        } catch (Exception e) {
            result.setResponseCode(ResponseCode.RESPONSE_ERROR);
            result.setDescription(e.getMessage());
        }
        return result;
    }
}
