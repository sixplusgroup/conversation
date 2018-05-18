package finley.gmair.service;

import finley.gmair.util.ResultData;
import org.springframework.cloud.netflix.feign.FeignClient;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;

@FeignClient(name = "message-agent")
public interface MessageService {
    @RequestMapping(method = RequestMethod.POST, value = "message/send/single")
    ResultData sendOne(@RequestParam("phone")String phone,
                              @RequestParam("text")String text);
}
