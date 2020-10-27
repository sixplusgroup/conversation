package finley.gmair.service;

import finley.gmair.util.ResultData;
import org.springframework.cloud.netflix.feign.FeignClient;
import org.springframework.web.bind.annotation.PostMapping;

/**
 * @author: Bright Chan
 * @date: 2020/10/27 9:48
 * @description: OrderNewFeignService
 */

@FeignClient("order-agent")
public interface OrderNewFeignService {

    @PostMapping("/order-new/tbOrderSync/import/incremental")
    ResultData incrementalImport();
}
