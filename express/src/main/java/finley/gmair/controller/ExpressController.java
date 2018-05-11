package finley.gmair.controller;

import finley.gmair.company.CompanyTransfer;
import finley.gmair.form.express.ExpressCompanyForm;
import finley.gmair.form.express.ExpressOrderForm;
import finley.gmair.form.express.ExpressParcelForm;
import finley.gmair.model.express.ExpressCompany;
import finley.gmair.model.express.ExpressOrder;
import finley.gmair.model.express.ExpressParcel;
import finley.gmair.model.express.ParcelType;
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

    @Autowired
    private CompanyTransfer companyTransfer;

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
    @GetMapping("/order/query/{orderId}")
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
    @GetMapping("/query/{expressNo}")
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
            List<ExpressOrder> list= (List<ExpressOrder>) response.getData();
            ExpressOrder expressOrder = list.get(0);
            condition.remove("expressNo");
            condition.put("companyId", expressOrder.getCompanyId());
            response = expressService.fetchExpressCompany(condition);
            if(response.getResponseCode() == ResponseCode.RESPONSE_OK){
                List<ExpressCompany> listCompany = (List<ExpressCompany>) response.getData();
                ExpressCompany expressCompany = listCompany.get(0);
                response = companyTransfer.transfer(expressCompany.getCompanyCode(), expressOrder.getExpressNo(), true);
                if(response.getResponseCode() == ResponseCode.RESPONSE_OK){
                    result.setData(response.getData());
                    result.setResponseCode(ResponseCode.RESPONSE_OK);
                    return result;
                }else{
                    return response;
                }
            }else{
                return response;
            }
        }else{
            return response;
        }
    }

    /**
     * This method is used to add parcel in the system
     *
     * @return
     */
    @PostMapping("/parcel/create")
    public ResultData addParcel(ExpressParcelForm form) {
        ResultData result = new ResultData();
        String parentExpress = form.getParentExpress().trim();
        String expressNo = form.getExpressNo().trim();
        String codeValue = form.getCodeValue().trim();
        ParcelType parcelType = form.getParcelType();
        if(StringUtils.isEmpty(parentExpress)||StringUtils.isEmpty(parcelType)){
            result.setResponseCode(ResponseCode.RESPONSE_ERROR);
            result.setDescription("Please make sure the parentExpress or parcelType is specified");
            return result;
        }
        if(StringUtils.isEmpty(codeValue)&&parcelType.getValue() == 0){
            result.setResponseCode(ResponseCode.RESPONSE_ERROR);
            result.setDescription("Please make sure the codeValue is specified");
            return result;
        }
        Map<String, Object> condition = new HashMap<>();
        ResultData response;
        if(!StringUtils.isEmpty(codeValue)) {
            condition.put("codeValue", codeValue);
            condition.put("blockflag", false);
            response = expressService.fetchExpressParcel(condition);
            if (response.getResponseCode() == ResponseCode.RESPONSE_OK) {
                result.setResponseCode(ResponseCode.RESPONSE_OK);
                result.setDescription(new StringBuffer("Express parcel belongs to order_express: ").append(parentExpress).append(" already exist").toString());
                return result;
            }
        }
        if(StringUtils.isEmpty(expressNo)){
            Map<String, Object> condition_order = new HashMap<>();
            condition_order.put("expressId", parentExpress);
            condition_order.put("blockFlag", false);
            ResultData response_order = expressService.fetchExpressOrder(condition_order);
            if (response_order.getResponseCode() == ResponseCode.RESPONSE_OK) {
                List<ExpressOrder> list= (List<ExpressOrder>) response_order.getData();
                expressNo=list.get(0).getExpressNo();
            }else{
                return response_order;
            }
        }
        ExpressParcel expressParcel = new ExpressParcel(parentExpress,expressNo,codeValue,parcelType);
        response = expressService.createExpressParcel(expressParcel);
        if (response.getResponseCode() == ResponseCode.RESPONSE_OK) {
            result.setResponseCode(ResponseCode.RESPONSE_OK);
            result.setData(response.getData());
        }
        if (response.getResponseCode() == ResponseCode.RESPONSE_ERROR) {
            result.setResponseCode(ResponseCode.RESPONSE_ERROR);
            result.setDescription(new StringBuffer("Fail to add express parcel belongs to: ").append(parentExpress).toString());
        }
        return result;
    }

    /**
     * This method is used to query orderId in the system
     *
     * @return
     */
    @GetMapping("/parcel/query/{codeValue}")
    public ResultData getOrderId(@PathVariable String codeValue) {
        ResultData result = new ResultData();
        if (StringUtils.isEmpty(codeValue)){
            result.setResponseCode(ResponseCode.RESPONSE_ERROR);
            result.setDescription("Please make sure the code_value is specified");
            return result;
        }
        Map<String, Object> condition = new HashMap<>();
        condition.put("codeValue", codeValue);
        condition.put("blockFlag", false);
        ResultData response = expressService.fetchExpressParcel(condition);
        if (response.getResponseCode() == ResponseCode.RESPONSE_OK) {
            ExpressParcel parcel = ((List<ExpressParcel>) response.getData()).get(0);
            condition.clear();
            condition.put("expressId", parcel.getParentExpress());
            response = expressService.fetchExpressOrder(condition);
            if(response.getResponseCode() == ResponseCode.RESPONSE_OK) {
                ExpressOrder order = ((List<ExpressOrder>) response.getData()).get(0);
                result.setResponseCode(ResponseCode.RESPONSE_OK);
                result.setData(order);
                return result;
            }else{
                return response;
            }

        }else{
            return response;
        }
    }
}
