package finley.gmair.service;

import finley.gmair.form.message.MessageForm;
import finley.gmair.util.ResultData;
import org.springframework.cloud.netflix.feign.FeignClient;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

@FeignClient("message-agent")
public interface MessageService {
    @RequestMapping(method = RequestMethod.POST, value = "/message/send/single")
    ResultData sendOne(MessageForm form);
}
