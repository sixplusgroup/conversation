package com.gmair.shop.service.feign;

import finley.gmair.util.ResultData;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

/**
 * @Author Joby
 */
@FeignClient("payment-agent")
public interface PayFeignService {

    @PostMapping("/payment/bill/create")
    ResultData createTrade(@RequestParam("orderId") String orderId, @RequestParam("openid") String openid, @RequestParam("price") int price, @RequestParam("body") String body, @RequestParam("ip") String ip);

}
