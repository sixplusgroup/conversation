package finley.gmair.company.impl;

import finley.gmair.company.ExpressCompanyService;
import finley.gmair.util.ResultData;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.PropertySource;
import org.springframework.stereotype.Service;

@Service
@PropertySource(value = "classpath:/expressCompany.properties")
public class SfService implements ExpressCompanyService {

    @Value("${SFappId}")
    private String appId;

    @Value("${SFappKey}")
    private String appKey;


    @Override
    public ResultData queryExpressStatus(String expressNo) {
        return null;
    }

    @Override
    public ResultData queryExpressRoute(String expressNo) {
        return null;
    }
}
