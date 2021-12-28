package finley.gmair.service;

import finley.gmair.model.drift.DriftOrderExpress;
import finley.gmair.util.ResultData;
import org.springframework.cloud.netflix.feign.FeignClient;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

/**
 * @author ：tsl
 * @date ：Created in 2020/10/26 15:15
 * @description ：
 */

@FeignClient(name = "drift-agent")
public interface DriftClient {
    @PostMapping("/drift/order/sync")
    ResultData syncCreate(@RequestBody DriftOrderExpress orderExpress);
}
