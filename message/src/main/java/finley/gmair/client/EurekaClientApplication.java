package finley.gmair.client;

import finley.gmair.form.message.MessageForm;
import finley.gmair.util.MessageUtil;
import finley.gmair.util.ResultData;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.netflix.eureka.EnableEurekaClient;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

@RestController
@EnableEurekaClient
@SpringBootApplication
@RequestMapping("/message")
public class EurekaClientApplication {
    public static void main(String[] args) {
        SpringApplication.run(EurekaClientApplication.class, args);
    }

    @RequestMapping(method = RequestMethod.POST, value = "/send/single")
    public ResultData sendOne(MessageForm form) {
        ResultData result = new ResultData();
        MessageUtil.sendOne(form.getPhone().trim(), form.getText().trim());
        return result;
    }

    @RequestMapping(method = RequestMethod.POST, value = "/send/group")
    public ResultData sendGroup(MessageForm form) {
        ResultData result = new ResultData();
        MessageUtil.sendGroup(form.getPhone().trim(), form.getText().trim());
        return result;
    }
}
