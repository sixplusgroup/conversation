package finley.gmair.dao;

import finley.gmair.model.installation.ExpressOrder;
import finley.gmair.util.ResultData;

import java.util.Map;

public interface ExpressDao {
    ResultData query(Map<String, Object> condition);

    ResultData insert(ExpressOrder express);

    ResultData update(Map<String, Object> condition);
}
