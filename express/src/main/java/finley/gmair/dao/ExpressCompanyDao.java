package finley.gmair.dao;

import finley.gmair.model.express.ExpressCompany;
import finley.gmair.util.ResultData;

import java.util.Map;

public interface ExpressCompanyDao {
    ResultData insertExpressCompany(ExpressCompany company);

    ResultData queryExpressCompany(Map<String, Object> condition);
}
