package finley.gmair.service;

import finley.gmair.util.ResultData;
import org.springframework.cloud.netflix.feign.FeignClient;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

@FeignClient("wechat-agent")
public interface WechatService {

    @PostMapping("/wechat/user/openid")
    ResultData openid(@RequestParam("code") String code);
}
