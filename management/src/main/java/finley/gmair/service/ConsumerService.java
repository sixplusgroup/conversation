package finley.gmair.service;

import finley.gmair.form.consumer.ConsumerPartInfoQuery;
import finley.gmair.util.ResultData;
import org.springframework.cloud.netflix.feign.FeignClient;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

/**
 * @author: Bright Chan
 * @date: 2020/12/26 15:00
 * @description: ConsumerService
 */

@FeignClient(value = "consumer-auth-agent", configuration = MachineService.RequestBodySupportConfig.class)
public interface ConsumerService {

    @PostMapping("/auth/consumer/accounts")
    ResultData queryConsumerAccounts(@RequestBody ConsumerPartInfoQuery query);
}
