package finley.gmair.service;

import finley.gmair.model.express.ExpressCompany;
import finley.gmair.model.express.ExpressOrder;
import finley.gmair.model.express.ExpressParcel;
import finley.gmair.util.ResultData;

import java.util.Map;

public interface ExpressService {
    ResultData createExpressCompany(ExpressCompany company);

    ResultData fetchExpressCompany(Map<String, Object> condition);

    ResultData createExpressOrder(ExpressOrder order, String[] qrcodeList);

    ResultData fetchExpressOrder(Map<String, Object> condition);

    ResultData confirmReceive(String expressId);

    ResultData createExpressParcel(ExpressParcel expressParcel);

    ResultData fetchExpressParcel(Map<String, Object> condition);

    ResultData deleteExpressOrder(String expressId);

    ResultData deleteExpressParcel(String expressId);

    ResultData fetchExpressToken(Map<String, Object> condition);
}
