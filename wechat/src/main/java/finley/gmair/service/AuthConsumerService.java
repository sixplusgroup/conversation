package finley.gmair.service;

import finley.gmair.util.ResultData;
import org.springframework.cloud.netflix.feign.FeignClient;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

@FeignClient("auth-consumer-agent")
public interface AuthConsumerService {

    @PostMapping("/auth/probe/consumerid/by/openid")
    ResultData findConsumer(@RequestParam("openId") String openId);

}
