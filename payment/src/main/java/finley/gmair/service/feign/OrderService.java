package finley.gmair.service.feign;

import finley.gmair.util.ResultData;
import org.springframework.cloud.netflix.feign.FeignClient;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;

@FeignClient("drift-agent")
public interface OrderService {

    @RequestMapping(value = "/drift/order/payed", method = RequestMethod.POST)
    ResultData updateOrderPayed(@RequestParam("orderId") String orderId);

}
