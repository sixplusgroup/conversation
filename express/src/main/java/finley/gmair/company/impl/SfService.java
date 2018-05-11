package finley.gmair.company.impl;

import finley.gmair.company.ExpressCompanyService;
import finley.gmair.util.ResponseCode;
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
        ResultData result = new ResultData();
        int status = -1;
        if(status != -1){
            result.setResponseCode(ResponseCode.RESPONSE_OK);
            result.setData(status);
        }else{
            result.setResponseCode(ResponseCode.RESPONSE_ERROR);
            result.setDescription(new StringBuffer("Failed to query express status of express:").append(expressNo).toString());
        }
        return result;
    }

    @Override
    public ResultData queryExpressRoute(String expressNo) {
        ResultData result = new ResultData();
        if(true){
            result.setResponseCode(ResponseCode.RESPONSE_OK);
            result.setData(new StringBuffer("route:...").toString());
        }else{
            result.setResponseCode(ResponseCode.RESPONSE_ERROR);
            result.setDescription(new StringBuffer("Failed to query express route of express:").append(expressNo).toString());

        }
        return result;
    }
}
