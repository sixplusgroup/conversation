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

    ResultData deleteExpressOrder(String orderId);

    ResultData deleteExpressParcel(String parent_express);
}
