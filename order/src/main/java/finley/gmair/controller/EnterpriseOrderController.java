package finley.gmair.controller;

import finley.gmair.model.order.EnterpriseOrder;
import finley.gmair.service.EnterpriseOrderService;
import finley.gmair.util.ResponseCode;
import finley.gmair.util.ResultData;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping("/enterprise/order")
public class EnterpriseOrderController {

    @Autowired
    private EnterpriseOrderService enterpriseOrderService;

    @PostMapping("/create")
    public ResultData createEnterpriseOrder(String merchantId,String modelName,boolean requireInstall,boolean planConfirmed,String description){
        ResultData result = new ResultData();
        if(StringUtils.isEmpty(merchantId) || StringUtils.isEmpty(modelName)){
            result.setResponseCode(ResponseCode.RESPONSE_ERROR);
            result.setDescription("please provide the merchantId and modelName");
            return result;
        }
        EnterpriseOrder enterpriseOrder = new EnterpriseOrder(merchantId,modelName,requireInstall,planConfirmed,description);
        ResultData response = enterpriseOrderService.create(enterpriseOrder);
        if(response.getResponseCode() == ResponseCode.RESPONSE_ERROR){
            result.setResponseCode(ResponseCode.RESPONSE_ERROR);
            result.setDescription("fail to create enterprise order");
            return result;
        }
        result.setDescription("success to create enterprise order");
        return result;
    }

    @GetMapping("/probe")
    public ResultData probeEnterpriseOrder(String merchantId){
        ResultData result = new ResultData();
        if(StringUtils.isEmpty(merchantId)){
            result.setResponseCode(ResponseCode.RESPONSE_ERROR);
            result.setDescription("please provide all the input");
            return result;
        }
        Map<String, Object> condition = new HashMap<>();
        condition.put("merchantId",merchantId);
        condition.put("blockFlag",false);
        ResultData response = enterpriseOrderService.fetch(condition);
        if(response.getResponseCode()==ResponseCode.RESPONSE_ERROR){
            result.setResponseCode(ResponseCode.RESPONSE_ERROR);
            result.setDescription("fail to probe");
            return result;
        }else if(response.getResponseCode()==ResponseCode.RESPONSE_NULL){
            result.setResponseCode(ResponseCode.RESPONSE_NULL);
            result.setDescription("not find");
            return result;
        }else if(response.getResponseCode()==ResponseCode.RESPONSE_OK){
            result.setResponseCode(ResponseCode.RESPONSE_OK);
            result.setData(response.getData());
            result.setDescription("success to probe");
            return result;
        }
        return result;
    }
}
