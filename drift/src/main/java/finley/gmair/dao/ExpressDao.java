package finley.gmair.dao;

import finley.gmair.model.drift.Express;
import finley.gmair.util.ResultData;

import java.util.Map;

public interface ExpressDao {
    ResultData queryExpress(Map<String, Object> condition);

    ResultData insertExpress(Express express);

    ResultData updateExpress(Map<String, Object> condition);
}
