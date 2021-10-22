package com.gmair.shop.service.feign;

import finley.gmair.form.consumer.ConsumerForm;
import finley.gmair.util.ResponseData;
import finley.gmair.util.ResultData;
import finley.gmair.vo.consumer.ConsumerPartInfoVo;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.*;

/**
 * @Author Joby
 */
@FeignClient("consumer-auth-agent")
public interface UserFeignService {

    @GetMapping("/auth/probe/consumerInfo/by/phone")
    ResponseData<ConsumerPartInfoVo> getConsumerInfoByPhone(@RequestParam("phone") String phone);

    @PostMapping("/auth/consumer/register")
    ResultData consumerRegister(@RequestParam("name") String name,@RequestParam("username") String username,@RequestParam("phone") String phone,@RequestParam("wechat") String wechat);

}
