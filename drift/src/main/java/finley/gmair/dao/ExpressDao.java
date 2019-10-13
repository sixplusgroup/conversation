package finley.gmair.dao;

import finley.gmair.model.drift.DriftExpress;
import finley.gmair.util.ResultData;

import java.util.Map;

public interface ExpressDao {
    ResultData queryExpress(Map<String, Object> condition);

    ResultData insertExpress(DriftExpress driftExpress);

    ResultData update(Map<String, Object> condition);
}
