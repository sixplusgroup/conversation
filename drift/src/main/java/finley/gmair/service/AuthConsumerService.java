package finley.gmair.service;

import finley.gmair.form.consumer.ConsumerForm;
import finley.gmair.form.consumer.LoginForm;
import finley.gmair.util.ResultData;
import org.springframework.cloud.netflix.feign.FeignClient;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

@FeignClient("consumer-auth-agent")
public interface AuthConsumerService {

    @PostMapping(value = "/auth/consumer/register")
    ResultData register(@RequestBody ConsumerForm form);

    @PostMapping(value = "/auth/consumer/login")
    ResultData login(@RequestBody LoginForm form);
}
