package finley.gmair.service;

import finley.gmair.util.ResultData;
import org.springframework.cloud.netflix.feign.FeignClient;
import org.springframework.web.bind.annotation.PostMapping;

/**
 * @author ：tsl
 * @date ：Created in 2021-12-25 16:45
 * @description ：
 */

@FeignClient("order-centre-agent")
public interface OrderCentreFeignService {
    @PostMapping("/order-centre/trade/schedulePullAll")
    ResultData schedulePullAll();
}
