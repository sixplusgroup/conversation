package finley.gmair.company.impl;

import finley.gmair.company.ExpressCompanyService;
import finley.gmair.util.ResponseCode;
import finley.gmair.util.ResultData;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.PropertySource;
import org.springframework.stereotype.Service;

@Service
@PropertySource(value = "classpath:/express.properties")
public class JdService implements ExpressCompanyService {

    @Value("${JDappId}")
    private String JDappId;

    @Value("${JDappKey}")
    private String JDappKey;


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
