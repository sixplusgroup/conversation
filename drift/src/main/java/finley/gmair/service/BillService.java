package finley.gmair.service;

import finley.gmair.util.ResultData;
import org.springframework.cloud.netflix.feign.FeignClient;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

@FeignClient("bill-agent")
public interface BillService {

    @PostMapping("/bill/create")
    ResultData createBill(@RequestParam("orderId") String orderId,
                          @RequestParam("orderPrice") double orderPrice,
                          @RequestParam("actualPrice") double actualPrice);
}
