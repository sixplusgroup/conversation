package finley.gmair.controller;

import finley.gmair.form.express.ExpressCompanyForm;
import finley.gmair.model.express.ExpressCompany;
import finley.gmair.service.ExpressService;
import finley.gmair.util.ResponseCode;
import finley.gmair.util.ResultData;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping("/express")
public class ExpressController {

    @Autowired
    private ExpressService expressService;

    /**
     * This method is used to add express company in the system
     *
     * @return
     */
    @PostMapping("/company/create")
    public ResultData addCompany(ExpressCompanyForm form) {
        ResultData result = new ResultData();
        String companyName = form.getCompanyName().trim();
        if (StringUtils.isEmpty(companyName)) {
            result.setResponseCode(ResponseCode.RESPONSE_ERROR);
            result.setDescription("Please make sure the express company name is specified");
        }
        Map<String, Object> condition = new HashMap<>();
        condition.put("companyName", companyName);
        condition.put("blockFlag", false);
        ResultData response = expressService.fetchExpressCompany(condition);
        if (response.getResponseCode() == ResponseCode.RESPONSE_OK) {
            result.setResponseCode(ResponseCode.RESPONSE_OK);
            result.setDescription(new StringBuffer("Express company: ").append(companyName).append(" already exist").toString());
            return result;
        }
        ExpressCompany company = new ExpressCompany(companyName);
        response = expressService.createExpressCompany(company);
        if (response.getResponseCode() == ResponseCode.RESPONSE_OK) {
            result.setResponseCode(ResponseCode.RESPONSE_OK);
            result.setData(response.getData());
        }
        if (response.getResponseCode() == ResponseCode.RESPONSE_ERROR) {
            result.setResponseCode(ResponseCode.RESPONSE_ERROR);
            result.setDescription(new StringBuffer("Fail to add express company: ").append(companyName).toString());
        }
        return result;
    }
}
