package finley.gmair.service;

import finley.gmair.model.drift.DriftOrderExpress;
import finley.gmair.util.ResultData;
import org.springframework.cloud.netflix.feign.FeignClient;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;

/**
 * @author ：tsl
 * @date ：Created in 2020/10/26 15:15
 * @description ：
 */

@FeignClient(name = "drift-agent")
public interface DriftOrderSyncService {
    @PostMapping("/drift/order/sync")
    ResultData syncOrderToDrift(@RequestBody DriftOrderExpress orderExpress);

    @PostMapping("/drift/order/sync/partInfo")
    ResultData syncOrderPartInfoToDrift(@RequestParam String orderId, @RequestParam String consignee,
                                     @RequestParam String phone, @RequestParam String address);
}
