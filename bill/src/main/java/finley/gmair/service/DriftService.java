package finley.gmair.service;

import finley.gmair.util.ResultData;
import org.springframework.cloud.netflix.feign.FeignClient;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

@FeignClient("drift-agent")
public interface DriftService {

    @PostMapping(value = "/drift/order/payed")
    ResultData orderPayed(@RequestParam("orderId") String orderId);
}
