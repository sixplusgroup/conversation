package finley.gmair.service;

import finley.gmair.form.message.MessageForm;
import finley.gmair.util.ResultData;
import org.springframework.cloud.netflix.feign.FeignClient;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

@FeignClient("message-agent")
public interface MessageService {

    @PostMapping("/message/send/single")
    ResultData sendOne(@RequestBody MessageForm form);

    @PostMapping("/message/send/group")
    ResultData sendGroup(@RequestBody MessageForm form);
}
