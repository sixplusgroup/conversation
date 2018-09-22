package finley.gmair.dao.impl;

import finley.gmair.dao.BaseDao;
import finley.gmair.dao.RepositoryDao;
import finley.gmair.model.drift.DriftRepository;
import finley.gmair.util.ResponseCode;
import finley.gmair.util.ResultData;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Map;

@Repository
public class RepositoryDaoImpl extends BaseDao implements RepositoryDao {
    @Override
    public ResultData queryRepository(Map<String, Object> condition) {
        ResultData result = new ResultData();
        try {
            List<DriftRepository> list = sqlSession.selectList("gmair.drift.repository.query", condition);
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
    public ResultData insertRepository(DriftRepository driftRepository) {
        ResultData result = new ResultData();
        try {
            sqlSession.insert("gmair.drift.repository.insert", driftRepository);
            result.setData(driftRepository);
        } catch (Exception e) {
            e.printStackTrace();
            result.setResponseCode(ResponseCode.RESPONSE_ERROR);
            result.setDescription(e.getMessage());
        }
        return result;
    }

    @Override
    public ResultData updateRepository(Map<String, Object> condition) {
        ResultData result = new ResultData();
        try {
            sqlSession.update("gmair.drift.repository.update", condition);
        } catch (Exception e) {
            e.printStackTrace();
            result.setResponseCode(ResponseCode.RESPONSE_ERROR);
            result.setDescription(e.getMessage());
        }
        return result;
    }
}
