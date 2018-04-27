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
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.List;
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
            return result;
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
        String companyName = form.getCompanyName().trim();
        String expressNo = form.getExpressNo().trim();
        if (StringUtils.isEmpty(orderId)||StringUtils.isEmpty(companyName)||StringUtils.isEmpty(expressNo )){
            result.setResponseCode(ResponseCode.RESPONSE_ERROR);
            result.setDescription("Please make sure the order id or express company or no is specified");
            return result;
        }
        Map<String, Object> condition_company = new HashMap<>();
        condition_company.put("companyName", companyName);
        ResultData response_company = expressService.fetchExpressCompany(condition_company);
        String companyId;
        if (response_company.getResponseCode() == ResponseCode.RESPONSE_OK) {
            List<ExpressCompany> list= (List<ExpressCompany>) response_company.getData();
            companyId=list.get(0).getCompanyId();
        }else{
            return response_company;
        }
        Map<String, Object> condition = new HashMap<>();
        condition.put("orderId", orderId);
        condition.put("blockFlag", false);
        ResultData response = expressService.fetchExpressOrder(condition);
        if (response.getResponseCode() == ResponseCode.RESPONSE_OK) {
            result.setResponseCode(ResponseCode.RESPONSE_OK);
            result.setDescription(new StringBuffer("Express order: ").append(orderId).append(" already exist").toString());
            return result;
        }
        condition.remove("orderId");
        condition.put("expressNo", expressNo);
        response = expressService.fetchExpressOrder(condition);
        if (response.getResponseCode() == ResponseCode.RESPONSE_OK) {
            result.setResponseCode(ResponseCode.RESPONSE_OK);
            result.setDescription(new StringBuffer("Express order_expressNo: ").append(expressNo).append(" already exist").toString());
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

    /**
     * This method is used to query express_order in the system
     *
     * @return
     */
    @PostMapping("/order/query/{orderId}")
    public ResultData queryOrder(@PathVariable String orderId) {
        ResultData result = new ResultData();
        if (StringUtils.isEmpty(orderId)){
            result.setResponseCode(ResponseCode.RESPONSE_ERROR);
            result.setDescription("Please make sure the order id is specified");
            return result;
        }
        Map<String, Object> condition = new HashMap<>();
        condition.put("orderId", orderId);
        condition.put("blockFlag", false);
        ResultData response = expressService.fetchExpressOrder(condition);
        if (response.getResponseCode() == ResponseCode.RESPONSE_OK) {
            List<ExpressOrder> list= (List<ExpressOrder>) response.getData();
            result.setData(list.get(0));
            result.setResponseCode(ResponseCode.RESPONSE_OK);
            return result;
        }else{
            return response;
        }
    }

    /**
     * This method is used to query express details in the system
     *
     * @return
     */
    @PostMapping("/query/{expressNo}")
    public ResultData queryRoute(@PathVariable String expressNo) {
        ResultData result = new ResultData();
        if (StringUtils.isEmpty(expressNo)){
            result.setResponseCode(ResponseCode.RESPONSE_ERROR);
            result.setDescription("Please make sure the express no is specified");
            return result;
        }
        Map<String, Object> condition = new HashMap<>();
        condition.put("expressNo", expressNo);
        condition.put("blockFlag", false);
        ResultData response = expressService.fetchExpressOrder(condition);
        if (response.getResponseCode() == ResponseCode.RESPONSE_OK) {
            /*
            获取快递路由信息
             */
            result.setData(null);
            result.setResponseCode(ResponseCode.RESPONSE_OK);
            return result;
        }else{

            return response;
        }
    }
}
