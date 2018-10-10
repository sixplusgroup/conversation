package finley.gmair.service;

import finley.gmair.util.ResultData;
import org.springframework.cloud.netflix.feign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;

@FeignClient("wechat-agent")
public interface WechatService {
    @GetMapping(value = "/accessToken/query")
    ResultData getToken();
}
