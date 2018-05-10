package finley.gmair.company;

import finley.gmair.util.ResultData;

public interface ExpressCompanyService {
    ResultData queryExpressStatus(String expressNo);

    ResultData queryExpressRoute(String expressNo);
}
