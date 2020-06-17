package finley.gmair.service;

import finley.gmair.util.ResultData;
import org.springframework.cloud.netflix.feign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

@FeignClient("message-agent")
public interface MessageService {

    @GetMapping("/message/template/{key}")
    ResultData template(@PathVariable("key") String key);

    @PostMapping("/message/send/single")
    ResultData send(@RequestParam("phone") String phone, @RequestParam("text") String text);

    @PostMapping("/message/send/single")
    ResultData send(@RequestParam("phone") String phone, @RequestParam("text") String text,@RequestParam("signature") String signature);
}
