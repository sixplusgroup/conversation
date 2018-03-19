package finley.gmair.client;

import com.netflix.discovery.converters.Auto;
import finley.gmair.form.message.MessageForm;
import finley.gmair.model.message.TextMessage;
import finley.gmair.service.MessageService;
import finley.gmair.util.MessageUtil;
import finley.gmair.util.ResultData;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
import org.springframework.cloud.netflix.eureka.EnableEurekaClient;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import java.lang.reflect.Array;
import java.util.Arrays;

@ComponentScan({"finley.gmair.service", "finley.gmair.dao"})
@RestController
@EnableDiscoveryClient
@EnableEurekaClient
@SpringBootApplication
@RequestMapping("/message")
public class EurekaClientApplication {
    public static void main(String[] args) {
        SpringApplication.run(EurekaClientApplication.class, args);
    }

    @Autowired
    private MessageService messageService;

    /**
     * This method is called to send message to a target user
     * phone number & message content should be passed
     * signature will be loaded from configuration file
     *
     * @param form
     * @return
     */
    @RequestMapping(method = RequestMethod.POST, value = "/send/single")
    public ResultData sendOne(MessageForm form) {
        ResultData result = new ResultData();
        MessageUtil.sendOne(form.getPhone().trim(), form.getText().trim());
        //save the message to database asynchronously
        TextMessage message = new TextMessage(form.getPhone().trim(), form.getText().trim());
        new Thread(() -> messageService.createTextMessage(message)).start();
        return result;
    }

    /**
     * This method is called to send message to a group of target user
     * phone number & message content should be passed
     * signature will be loaded from configuration file
     *
     * @param form
     * @return
     */
    @RequestMapping(method = RequestMethod.POST, value = "/send/group")
    public ResultData sendGroup(MessageForm form) {
        ResultData result = new ResultData();
        MessageUtil.sendGroup(form.getPhone().trim(), form.getText().trim());
        //save the message to database asynchronously
        String[] phones = form.getPhone().split(",");
        Arrays.stream(phones).forEach(item -> {
            TextMessage message = new TextMessage(item.trim(), form.getText().trim());
            new Thread(() -> messageService.createTextMessage(message)).start();
        });
        return result;
    }
}
