package finley.gmair.dao;

import finley.gmair.model.drift.DriftRepository;
import finley.gmair.util.ResultData;

import java.util.Map;

public interface RepositoryDao {
    ResultData queryRepository(Map<String, Object> condition);

    ResultData insertRepository(DriftRepository driftRepository);

    ResultData updateRepository(Map<String, Object> condition);
}
