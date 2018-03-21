package finley.gmair.client;

import finley.gmair.form.message.MessageForm;
import finley.gmair.model.message.TextMessage;
import finley.gmair.service.MessageService;
import finley.gmair.util.MessageUtil;
import finley.gmair.util.ResponseCode;
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

import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;

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

    /**
     * This method is called to fetch the message records
     * it will return the whole record list by default
     *
     * @return
     */
    @RequestMapping(method = RequestMethod.POST, value = "/sent/overview")
    public ResultData overview() {
        ResultData result = new ResultData();
        Map<String, Object> condition = new HashMap<>();
        ResultData response = messageService.fetchTextMessage(condition);
        if (response.getResponseCode() == ResponseCode.RESPONSE_OK) {
            result.setResponseCode(ResponseCode.RESPONSE_OK);
            result.setData(response.getData());
        } else if (response.getResponseCode() == ResponseCode.RESPONSE_NULL) {
            result.setResponseCode(ResponseCode.RESPONSE_NULL);
            result.setDescription("No record retrieved from database");
        } else {
            result.setResponseCode(ResponseCode.RESPONSE_ERROR);
            result.setDescription("Failed to load message record from database");
        }
        return result;
    }
}
