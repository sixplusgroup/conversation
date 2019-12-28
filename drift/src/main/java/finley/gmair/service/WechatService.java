package finley.gmair.service;

import finley.gmair.util.ResultData;
import org.springframework.cloud.netflix.feign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

@FeignClient("wechat-agent")
public interface WechatService {

    @GetMapping("/wechat/message/confirmedMessage")
    ResultData confirmMessage(@RequestParam("orderId")String orderId);

    @GetMapping("/wechat/message/deliverMessage")
    ResultData deliverMessage(@RequestParam("orderId")String orderId,@RequestParam("wechat")String wechat,@RequestParam("expressOutNum")String expressOutNum,@RequestParam("expressOutCompany")String expressOutCompany);

    @GetMapping("/wechat/message/returnMessage")
    ResultData returnMessage(@RequestParam("orderId")String orderId,@RequestParam("wechat")String wechat,@RequestParam("activityName")String activityName,@RequestParam("expectDate")String expectDate);
}