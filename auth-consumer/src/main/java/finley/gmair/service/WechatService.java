package finley.gmair.service;

import finley.gmair.util.ResultData;
import org.springframework.cloud.netflix.feign.FeignClient;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

@FeignClient("wechat-agent")
public interface WechatService {

    @PostMapping("/wechat/user/getByOpenId")
    ResultData findConsumer(@RequestParam("openId") String openId);
}
