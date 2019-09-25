package finley.gmair.controller;


import finley.gmair.service.DriftService;
import finley.gmair.util.ResponseCode;
import finley.gmair.util.ResultData;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.print.DocFlavor;

@CrossOrigin
@RestController
@RequestMapping("/management/drift")
public class DriftController {

    @Autowired
    private DriftService driftService;

    @GetMapping("/order/list")
    ResultData driftOrderList(String startTime,String endTime,String status){
        ResultData result = new ResultData();
        if(StringUtils.isEmpty(startTime)&&StringUtils.isEmpty(endTime)&&StringUtils.isEmpty(status)){
            result.setResponseCode(ResponseCode.RESPONSE_ERROR);
            result.setDescription("请提供正确的参数");
            return result;
        }
        result = driftService.driftOrderList(startTime,endTime,status);
        return result;
    }

    @GetMapping("/order/{orderId}")
    ResultData selectByOrderId(@PathVariable("orderId") String orderId){
        ResultData result = new ResultData();
        if(StringUtils.isEmpty(orderId)){
            result.setResponseCode(ResponseCode.RESPONSE_ERROR);
            result.setDescription("请提供orderId");
            return result;
        }
        result = driftService.selectByOrderId(orderId);
        return result;
    }

    @PostMapping("/order/express/submit")
    ResultData submitMachineCode(String orderId,String machineCode,String expressNo, int expressFlag, String company){
        ResultData result = new ResultData();
        if(StringUtils.isEmpty(orderId)||StringUtils.isEmpty(machineCode)||StringUtils.isEmpty(expressNo)||StringUtils.isEmpty(company)){
            result.setResponseCode(ResponseCode.RESPONSE_ERROR);
            result.setDescription("请提供正确的参数");
            return result;
        }
        ResultData response = driftService.updateMachineCode(orderId,machineCode);
        if(response.getResponseCode()!=ResponseCode.RESPONSE_OK){
            result.setResponseCode(response.getResponseCode());
            result.setDescription("更新机器码失败");
            return result;
        }
        response = driftService.createOrderExpress(orderId,expressNo,expressFlag,company);
        if(response.getResponseCode()!=ResponseCode.RESPONSE_OK){
            result.setResponseCode(response.getResponseCode());
            result.setDescription("物流信息创建失败");
            return result;
        }
        result.setResponseCode(response.getResponseCode());
        result.setData(response.getData());
        result.setDescription(response.getDescription());
        return result;
    }
}
