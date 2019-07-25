package finley.gmair.service;

import finley.gmair.util.ResultData;
import org.springframework.cloud.netflix.feign.FeignClient;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

@FeignClient("payment-agent")
public interface PaymentService {
    @PostMapping("/payment/bill/create")
    ResultData createPay(@RequestParam("orderId") String orderId, @RequestParam("openid") String openid, @RequestParam("price") int price, @RequestParam("body") String body, @RequestParam("ip") String ip);

}
