package finley.gmair.controller;

import finley.gmair.form.express.ExpressCompanyForm;
import finley.gmair.form.express.ExpressOrderForm;
import finley.gmair.model.express.ExpressCompany;
import finley.gmair.model.express.ExpressOrder;
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
        String companyCode = form.getCompanyCode().trim();
        if (StringUtils.isEmpty(companyName)||StringUtils.isEmpty(companyCode)) {
            result.setResponseCode(ResponseCode.RESPONSE_ERROR);
            result.setDescription("Please make sure the express company name or code is specified");
        }
        Map<String, Object> condition = new HashMap<>();
        condition.put("companyName", companyName);
        condition.put("companyCode", companyCode);
        condition.put("blockFlag", false);
        ResultData response = expressService.fetchExpressCompany(condition);
        if (response.getResponseCode() == ResponseCode.RESPONSE_OK) {
            result.setResponseCode(ResponseCode.RESPONSE_OK);
            result.setDescription(new StringBuffer("Express company: ").append(companyName).append(" already exist").toString());
            return result;
        }
        ExpressCompany company = new ExpressCompany(companyName,companyCode);
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

    /**
     * This method is used to add express order in the system
     *
     * @return
     */
    @PostMapping("/order/create")
    public ResultData addOrder(ExpressOrderForm form) {
        ResultData result = new ResultData();
        String orderId = form.getOrderId().trim();
        String companyId = form.getCompanyId().trim();
        String expressNo = form.getExpressNo().trim();
        if (StringUtils.isEmpty(orderId)||StringUtils.isEmpty(companyId)||StringUtils.isEmpty(expressNo )){
            result.setResponseCode(ResponseCode.RESPONSE_ERROR);
            result.setDescription("Please make sure the order id or express company or no is specified");
        }
        Map<String, Object> condition = new HashMap<>();
        condition.put("orderId", orderId);
        condition.put("companyId", companyId);
        condition.put("expressNo", expressNo);
        condition.put("blockFlag", false);
        ResultData response = expressService.fetchExpressOrder(condition);
        if (response.getResponseCode() == ResponseCode.RESPONSE_OK) {
            result.setResponseCode(ResponseCode.RESPONSE_OK);
            result.setDescription(new StringBuffer("Express order: ").append(orderId).append(" already exist").toString());
            return result;
        }
        ExpressOrder order = new ExpressOrder(orderId, companyId, expressNo);
        response = expressService.createExpressOrder(order);
        if (response.getResponseCode() == ResponseCode.RESPONSE_OK) {
            result.setResponseCode(ResponseCode.RESPONSE_OK);
            result.setData(response.getData());
        }
        if (response.getResponseCode() == ResponseCode.RESPONSE_ERROR) {
            result.setResponseCode(ResponseCode.RESPONSE_ERROR);
            result.setDescription(new StringBuffer("Fail to add express order: ").append(orderId).toString());
        }
        return result;
    }
}
