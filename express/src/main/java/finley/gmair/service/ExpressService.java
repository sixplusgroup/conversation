package finley.gmair.service;

import finley.gmair.model.express.ExpressCompany;
import finley.gmair.util.ResultData;

import java.util.Map;

public interface ExpressService {
    ResultData createExpressCompany(ExpressCompany company);

    ResultData fetchExpressCompany(Map<String, Object> condition);
}
