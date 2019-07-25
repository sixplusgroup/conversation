package finley.gmair.service;

import finley.gmair.util.ResultData;
import org.springframework.cloud.netflix.feign.FeignClient;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

@FeignClient("payment-agent")
public interface BillService {

    @PostMapping("/payment/bill/create")
    ResultData createBill(@RequestParam("orderId") String orderId, @RequestParam("price") int actualPrice);
}
