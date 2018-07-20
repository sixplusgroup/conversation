package finley.gmair.service;

import finley.gmair.util.ResultData;
import org.springframework.cloud.netflix.feign.FeignClient;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

@FeignClient("consumer-auth-agent")
public interface AuthConsumerService {

    @PostMapping("/auth/probe/consumerid/by/openid")
    ResultData findConsumer(@RequestParam("openid") String openid);

}
