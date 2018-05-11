package finley.gmair.company;

import finley.gmair.company.impl.JdService;
import finley.gmair.company.impl.SfService;
import finley.gmair.util.ResponseCode;
import finley.gmair.util.ResultData;
import org.springframework.stereotype.Component;

@Component
public class CompanyTransfer {
    public ResultData transfer(String companyCode, String expressNo){
        ResultData result = new ResultData();
        switch(companyCode){
            case "jd":
                JdService jdService = new JdService();
                result = jdService.queryExpressStatus(expressNo);
                break;
            case "sf":
                SfService sfService = new SfService();
                result = sfService.queryExpressStatus(expressNo);
                break;
            default:
                result.setResponseCode(ResponseCode.RESPONSE_ERROR);
                result.setDescription(new StringBuffer("Do not support status query of the company:").append(companyCode).toString());
                break;
        }
        return result;
    }

    public ResultData transfer(String companyCode, String expressNo, boolean isRoute){
        ResultData result = new ResultData();
        switch(companyCode){
            case "jd":
                JdService jdService = new JdService();
                result = jdService.queryExpressRoute(expressNo);
                break;
            case "sf":
                SfService sfService = new SfService();
                result = sfService.queryExpressRoute(expressNo);
                break;
            default:
                result.setResponseCode(ResponseCode.RESPONSE_ERROR);
                result.setDescription(new StringBuffer("Do not support route query of the company:").append(companyCode).toString());
                break;
        }
        return result;
    }
}
