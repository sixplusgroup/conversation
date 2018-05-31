package finley.gmair.service;

import finley.gmair.util.ResultData;
import org.springframework.cloud.netflix.feign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

@FeignClient("express-agent")
public interface ExpressService {

    @GetMapping("/express/company/query")
    ResultData companyQuery();

    @GetMapping("/express/order/query/{orderId}")
    ResultData orderQuery(@PathVariable("orderId") String orderId);
}
