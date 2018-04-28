package finley.gmair.dao;

import finley.gmair.model.express.ExpressOrder;
import finley.gmair.util.ResultData;

import java.util.Map;

public interface ExpressOrderDao {
    ResultData insertExpressOrder(ExpressOrder order);

    ResultData queryExpressOrder(Map<String, Object> condition);

    ResultData updateExpressOrder(Map<String, Object> condition);
}
