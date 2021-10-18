package com.gmair.shop.service.feign;

import finley.gmair.form.consumer.ConsumerForm;
import finley.gmair.util.ResponseData;
import finley.gmair.util.ResultData;
import finley.gmair.vo.consumer.ConsumerPartInfoVo;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

/**
 * @Author Joby
 */
@FeignClient("consumer-auth-agent")
public interface UserFeignService {

    @GetMapping("/auth/probe/consumerInfo/by/phone")
    ResponseData<ConsumerPartInfoVo> getConsumerInfoByPhone(@RequestParam("phone") String phone);

    @PostMapping("/auth/consumer/register")
    ResultData consumerRegister(@RequestParam("form") ConsumerForm form);

}
