package finley.gmair.dao.impl;

import finley.gmair.dao.BaseDao;
import finley.gmair.dao.RepositoryDao;
import finley.gmair.model.drift.DriftRepository;
import org.springframework.stereotype.Repository;
import finley.gmair.util.ResultData;

import java.util.Map;

@Repository
public class RepositoryDaoImpl extends BaseDao implements RepositoryDao {
    @Override
    public ResultData queryRepository(Map<String, Object> condition) {
        return null;
    }

    @Override
    public ResultData insertRepository(DriftRepository repository) {
        return null;
    }

    @Override
    public ResultData updateRepository(DriftRepository repository) {
        return null;
    }
}
