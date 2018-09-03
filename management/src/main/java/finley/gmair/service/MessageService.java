package finley.gmair.service;

import finley.gmair.util.ResultData;
import org.springframework.cloud.netflix.feign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

@FeignClient("message-agent")
public interface MessageService {
    @PostMapping("/message/send/single")
    ResultData sendMessage(@RequestParam("phone") String phone,
                           @RequestParam("text") String text);

    @GetMapping("/message/template/list")
    ResultData findMessageTemplate();

    @PostMapping("/message/send/group")
    ResultData sendGroup(@RequestParam("phone") String phone,
                         @RequestParam("text") String text);
}
