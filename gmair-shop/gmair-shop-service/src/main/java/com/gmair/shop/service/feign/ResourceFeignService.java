package com.gmair.shop.service.feign;

import finley.gmair.util.ResultData;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

/**
 * @Author Joby
 */
@FeignClient("resource-agent")
public interface ResourceFeignService {
    @PostMapping("/resource/createpic")
    ResultData save(@RequestParam("fileUrl") String fileUrl);
}
