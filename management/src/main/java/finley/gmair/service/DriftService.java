package finley.gmair.service;

import finley.gmair.util.ResultData;
import org.springframework.cloud.netflix.feign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

@FeignClient("drift-agent")
public interface DriftService {

    @GetMapping("/drift/order/list")
    ResultData driftOrderList(@RequestParam("startTime") String startTime,@RequestParam("endTime") String endTime,@RequestParam("status") String status,@RequestParam("search") String search);

    @GetMapping("/drift/order/{orderId}")
    ResultData selectByOrderId(@PathVariable("orderId") String orderId);

    @PostMapping("/drift/order/machinecode/submit")
    ResultData updateMachineCode(@RequestParam("orderId") String orderId,@RequestParam("machineCode") String machineCode);

    @PostMapping(value = "drift/order/express/create")
    ResultData createOrderExpress(@RequestParam("orderId") String orderId, @RequestParam("expressNo") String expressNo, @RequestParam("expressFlag") int expressFlag, @RequestParam("company") String company);

    @GetMapping("/drift/activity/{activityId}/profile")
    ResultData getActivityDetail(@PathVariable("activityId") String activityId);

    @PostMapping("/drift/order/cancel")
    ResultData cancelOrder(@RequestParam("orderId") String orderId,@RequestParam("member") String member);

    @GetMapping("/drift/order/express/list")
    ResultData getExpressDetail(@RequestParam("orderId") String orderId,@RequestParam("status") int status);

    @PostMapping("/drift/order/update")
    ResultData updateOrder(@RequestParam("orderId") String orderId,@RequestParam("consignee") String consignee,
                           @RequestParam("phone") String phone,@RequestParam("province") String province,
                           @RequestParam("city") String city,@RequestParam("district") String district,
                           @RequestParam("address") String address,@RequestParam("status") String status);

    @PostMapping("/drift/order/changeStatus")
    ResultData changeStatus(@RequestParam("orderId") String orderId,@RequestParam("machineOrderNo") String machineOrderNo,@RequestParam("expressNum") String expressNum,@RequestParam("company") String company,@RequestParam("description") String description);

    @PostMapping("/drift/order/action/create")
    ResultData createAction(@RequestParam("orderId") String orderId,@RequestParam("message") String message,@RequestParam("member") String member);

    @GetMapping("/drift/order/action/select")
    ResultData selectAction(@RequestParam("orderId") String orderId);

    @GetMapping("drift/order/cancel/record/select")
    ResultData selectCancelRecord(@RequestParam("status") String status);

    @PostMapping("/drift/order/cancel/record/update")
    ResultData updateCancelRecord(@RequestParam("orderId")String orderId);
}
