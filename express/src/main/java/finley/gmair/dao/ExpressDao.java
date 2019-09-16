package finley.gmair.dao;

import finley.gmair.model.express.Express;
import finley.gmair.util.ResultData;

import java.util.Map;

public interface ExpressDao {
    ResultData fetch(Map<String, Object> condition);

    ResultData insert(Express express);

    ResultData update(Express express);
}
