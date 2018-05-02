package finley.gmair.dao;

import finley.gmair.model.express.ExpressParcel;
import finley.gmair.util.ResultData;

import java.util.Map;


public interface ExpressParcelDao {
    ResultData queryExpressParcel(Map<String, Object> condition);

    ResultData insertExpressParcel(ExpressParcel expressParcel);
}
