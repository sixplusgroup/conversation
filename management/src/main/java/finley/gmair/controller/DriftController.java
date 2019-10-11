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
        return driftService.driftOrderList(startTime,endTime,status);
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

    /**
     * 根据activityId查询活动详情
     * @param activityId
     * @return
     */
    @GetMapping("/activity/{activityId}/profile")
    ResultData getActivityDetail(@PathVariable("activityId") String activityId){
        return driftService.getActivityDetail(activityId);
    }

    /**
     * 取消订单
     * @param orderId
     * @return
     */
    @PostMapping("/order/cancel")
    ResultData cancelOrder(String orderId){
        ResultData result = new ResultData();
        if(StringUtils.isEmpty(orderId)){
            result.setResponseCode(ResponseCode.RESPONSE_ERROR);
            result.setDescription("请提供订单编号");
            return result;
        }
        result = driftService.cancelOrder(orderId);
        return result;
    }

    /**
     * 获取快递详情信息
     * @param orderId
     * @param status
     * @return
     */
    @GetMapping("/order/express/select")
    ResultData getExpressDetail(String orderId,int status){
        return driftService.getExpressDetail(orderId,status);
    }

    /**
     * 更新订单信息
     * @param orderId
     * @param consignee
     * @param phone
     * @param province
     * @param city
     * @param district
     * @param address
     * @param status
     * @return
     */
    @PostMapping("/order/update")
    ResultData updateOrder(String orderId,String consignee,String phone,String province,String city,String district,String address,String status){
        ResultData result = new ResultData();
        if(StringUtils.isEmpty(orderId)){
            result.setResponseCode(ResponseCode.RESPONSE_ERROR);
            result.setDescription("请提供orderId");
            return result;
        }
        result = driftService.updateOrder(orderId,consignee,phone,province,city,district,address,status);
        return result;
    }
}
