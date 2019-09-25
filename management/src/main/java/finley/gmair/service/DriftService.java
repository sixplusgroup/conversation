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
    ResultData driftOrderList(@RequestParam("startTime") String startTime,@RequestParam("endTime") String endTime,@RequestParam("status") String status);

    @GetMapping("/drift/order/{orderId}")
    ResultData selectByOrderId(@PathVariable("orderId") String orderId);

    @PostMapping("/drift/order/machinecode/submit")
    ResultData updateMachineCode(@RequestParam("orderId") String orderId,@RequestParam("machineCode") String machineCode);

    @PostMapping(value = "drift/order/express/create")
    ResultData createOrderExpress(@RequestParam("orderId") String orderId, @RequestParam("expressNo") String expressNo, @RequestParam("expressFlag") int expressFlag, @RequestParam("company") String company);
}
