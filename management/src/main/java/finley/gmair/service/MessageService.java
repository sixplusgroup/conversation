package finley.gmair.service;

import finley.gmair.util.ResultData;
import org.springframework.cloud.netflix.feign.FeignClient;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

@FeignClient("message-agent")
public interface MessageService {
    @PostMapping("/message/send/single")
    ResultData sendMessage(@RequestParam("phone") String phone,
                           @RequestParam("text") String text);
}
