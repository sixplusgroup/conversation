package finley.gmair.service;

import finley.gmair.util.ResultData;
import org.springframework.cloud.netflix.feign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;

@FeignClient("drift-agent")
public interface DriftFeignService {

    @GetMapping("/drift/order/notify/return")
    ResultData orderReturnMessage();
}
