package com.gmair.shop.service.feign;

import finley.gmair.util.ResultData;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

/**
 * @Author Joby
 */
@FeignClient("consumer-auth-agent")
public interface UserFeignService {

    @GetMapping("/auth/probe/consumerid/by/phone")
    ResultData getConsumerIdByPhone(@RequestParam("phone") String phone);

}
