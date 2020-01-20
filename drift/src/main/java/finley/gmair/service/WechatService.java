package finley.gmair.service;

import com.alibaba.fastjson.JSONObject;
import finley.gmair.util.ResultData;
import org.springframework.cloud.netflix.feign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

@FeignClient("wechat-agent")
public interface WechatService {

    @GetMapping("/wechat/message/sendMessage")
    ResultData sendMessage(@RequestParam("type")int type, @RequestParam("json") String json, @RequestParam("orderId")String orderId);

}