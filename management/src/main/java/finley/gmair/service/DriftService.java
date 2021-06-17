package finley.gmair.service;

import feign.codec.Encoder;
import feign.form.spring.SpringFormEncoder;
import finley.gmair.form.drift.DriftOrderForm;
import finley.gmair.model.ordernew.TradeFrom;
import finley.gmair.util.ResultData;
import org.springframework.beans.factory.ObjectFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.web.HttpMessageConverters;
import org.springframework.cloud.netflix.feign.FeignClient;
import org.springframework.cloud.netflix.feign.support.SpringEncoder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;

@FeignClient("drift-agent")
public interface DriftService {

    @GetMapping("/drift/order/list")
    ResultData driftOrderList(@RequestParam("startTime") String startTime,@RequestParam("endTime") String endTime,@RequestParam("status") String status,@RequestParam("search") String search,@RequestParam("type") String type);

    @GetMapping("/drift/order/listByPage")
    ResultData driftOrderListByPage(@RequestParam("curPage") int curPage,@RequestParam("pageSize") int pageSize,@RequestParam("startTime") String startTime,@RequestParam("endTime") String endTime,@RequestParam("status") String status,@RequestParam("search") String search,@RequestParam("type") String type);

    @GetMapping("/drift/order/{orderId}")
    ResultData selectByOrderId(@PathVariable("orderId") String orderId);

    @PostMapping("/drift/order/machinecode/submit")
    ResultData updateMachineCode(@RequestParam("orderId") String orderId,@RequestParam("machineCode") String machineCode);

    @PostMapping(value = "drift/order/express/create")
    ResultData createOrderExpress(@RequestParam("orderId") String orderId, @RequestParam("expressNo") String expressNo, @RequestParam("expressFlag") int expressFlag, @RequestParam("company") String company);

    @GetMapping("/drift/activity/{activityId}/profile")
    ResultData getActivityDetail(@PathVariable("activityId") String activityId);

    @PostMapping("/drift/order/cancel")
    ResultData cancelOrder(@RequestParam("orderId") String orderId,@RequestParam("account") String account);

    @GetMapping("/drift/order/express/list")
    ResultData getExpressDetail(@RequestParam("orderId") String orderId,@RequestParam("status") int status);

    @PostMapping("/drift/order/update")
    ResultData updateOrder(@RequestParam("orderId") String orderId,@RequestParam("consignee") String consignee,
                           @RequestParam("phone") String phone,@RequestParam("province") String province,
                           @RequestParam("city") String city,@RequestParam("district") String district,
                           @RequestParam("address") String address,@RequestParam("status") String status,
                           @RequestParam("expectedDate") String expectedDate);

    @PostMapping("/drift/order/changeStatus")
    ResultData changeStatus(@RequestParam("orderId") String orderId,@RequestParam("machineOrderNo") String machineOrderNo,@RequestParam("expressNum") String expressNum,@RequestParam("company") String company,@RequestParam("description") String description,@RequestParam("account") String account);

    @PostMapping("/drift/order/action/create")
    ResultData createAction(@RequestParam("orderId") String orderId,@RequestParam("message") String message,@RequestParam("member") String member);

    @GetMapping("/drift/order/action/select")
    ResultData selectAction(@RequestParam("orderId") String orderId);

    @GetMapping("drift/order/cancel/record/select")
    ResultData selectCancelRecord(@RequestParam("status") String status);

    @PostMapping("/drift/order/cancel/record/update")
    ResultData updateCancelRecord(@RequestParam("orderId")String orderId);

    @GetMapping("/drift/activity/{activityId}/available")
    ResultData getActivityAvailable(@PathVariable("activityId") String activityId);

    @GetMapping("/drift/activity/list")
    ResultData getActivityList();

    @PostMapping("/drift/activity/excode/create")
    ResultData createExcode(@RequestParam("activityId") String activityId,@RequestParam("num") int num,@RequestParam("price") double price,@RequestParam("status") int status,@RequestParam("label") String label);

    @GetMapping("drift/activity/excode/one/create")
    ResultData createOneExcode(@RequestParam("activityId") String activityId,@RequestParam("codeValue") String codeValue,@RequestParam("price") double price,@RequestParam("status") int status,@RequestParam("label") String label);

    @GetMapping("/drift/activity/excode/search")
    ResultData getExcodeByLabel(@RequestParam("activityId") String activityId,@RequestParam("label") String label);

    @GetMapping("/drift/activity/excode/query/label")
    ResultData getExcodeLabels(@RequestParam("activityId") String activityId);

    @PostMapping(value = "/drift/order/create")
    ResultData createDriftOrder(@RequestParam("consumerId") String consumerId, @RequestParam("activityId") String activityId,
                                @RequestParam("equipId") String equipId, @RequestParam("consignee") String consignee,
                                @RequestParam("phone") String phone, @RequestParam("address") String address, @RequestParam("province") String province,
                                @RequestParam("city") String city, @RequestParam("district") String district, @RequestParam("description") String description,
                                @RequestParam("expectedDate") String expectedDate, @RequestParam("intervalDate") int intervalDate,
                                @RequestParam("attachItem") String attachItem, @RequestParam("tradeFrom") TradeFrom tradeFrom);
}
