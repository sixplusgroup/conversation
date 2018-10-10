package finley.gmair.dao.impl;

import finley.gmair.dao.BaseDao;
import finley.gmair.dao.EXCodeDao;
import finley.gmair.model.drift.EXCode;
import finley.gmair.util.IDGenerator;
import finley.gmair.util.ResponseCode;
import finley.gmair.util.ResultData;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Map;

@Repository
public class EXCodeDaoImpl extends BaseDao implements EXCodeDao {

    @Override
    public ResultData insert(EXCode code) {
        ResultData result = new ResultData();
        code.setCodeId(IDGenerator.generate("EXC"));
        try {
            sqlSession.insert("gmair.drift.excode.insert", code);
            result.setData(code);
        } catch (Exception e) {
            e.printStackTrace();
            result.setResponseCode(ResponseCode.RESPONSE_ERROR);
            result.setDescription(e.getMessage());
        }
        return result;
    }

    @Override
    public ResultData query(Map<String, Object> condition) {
        ResultData result = new ResultData();
        try {
            List<EXCode> list = sqlSession.selectList("gmair.drift.excode.query", condition);
            if (list.isEmpty()) {
                result.setResponseCode(ResponseCode.RESPONSE_NULL);
            }
            result.setData(list);
        } catch (Exception e) {
            e.printStackTrace();
            result.setResponseCode(ResponseCode.RESPONSE_ERROR);
            result.setDescription(e.getMessage());
        }
        return result;
    }

    @Override
    public ResultData update(Map<String, Object> condition) {
        ResultData result = new ResultData();
        try {
            sqlSession.update("gmair.drift.excode.update", condition);
        } catch (Exception e) {
            e.printStackTrace();
            result.setResponseCode(ResponseCode.RESPONSE_ERROR);
            result.setDescription(e.getMessage());
        }
        return result;
    }
}
