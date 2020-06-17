package finley.gmair.service;

import finley.gmair.util.ResultData;
import org.springframework.cloud.netflix.feign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

@FeignClient("consumer-auth-agent")
public interface AuthConsumerService {


    @GetMapping("/auth/probe/wechat/by/phone")
    ResultData probeWechatByPhone(@RequestParam("phone")String phone);

}