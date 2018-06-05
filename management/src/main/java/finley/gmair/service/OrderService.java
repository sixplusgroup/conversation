package finley.gmair.service;

import finley.gmair.util.ResultData;
import org.springframework.cloud.netflix.feign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

@FeignClient("order-agent")
public interface OrderService {

    @GetMapping("/order/channel/list")
    ResultData channelList();

    @GetMapping("/order/list")
    ResultData orderList();

    @PostMapping("/order/create")
    ResultData orderCreate(@RequestParam("order") String order);

    @GetMapping("/order/{orderId}/info")
    ResultData orderInfo(@PathVariable("orderId") String orderId);

    @GetMapping("/order/commodityList")
    ResultData commodityList();

    @PostMapping("/order/deliver")
    ResultData orderDeliver(@RequestParam("orderId") String orderId);
}
