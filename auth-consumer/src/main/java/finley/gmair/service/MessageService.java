package finley.gmair.service;

import finley.gmair.form.message.MessageForm;
import finley.gmair.util.ResultData;
import org.springframework.cloud.netflix.feign.FeignClient;
import org.springframework.web.bind.annotation.RequestMapping;

@FeignClient("message-agent")
public interface MessageService {
    @RequestMapping("/message/send/single")
    ResultData sendOne(MessageForm form);
}
