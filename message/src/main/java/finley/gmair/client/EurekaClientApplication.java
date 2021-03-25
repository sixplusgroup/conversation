package finley.gmair.client;

import finley.gmair.form.message.MessageForm;
import finley.gmair.form.message.MessageTemplateForm;
import finley.gmair.model.message.MessageCatalog;
import finley.gmair.model.message.MessageTemplate;
import finley.gmair.model.message.TextMessage;
import finley.gmair.service.MessageService;
import finley.gmair.service.MessageTemplateService;
import finley.gmair.util.MessageProperties;
import finley.gmair.util.MessageUtil;
import finley.gmair.util.ResponseCode;
import finley.gmair.util.ResultData;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.jdbc.DataSourceAutoConfiguration;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
import org.springframework.cloud.netflix.eureka.EnableEurekaClient;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.*;

import javax.ws.rs.Path;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;

@ComponentScan({"finley.gmair.controller", "finley.gmair.service", "finley.gmair.dao"})
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

    @Autowired
    private MessageTemplateService messageTemplateService;

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
        String text;
        if (StringUtils.isEmpty(form.getSignature())) {
            text = new StringBuffer(form.getText()).append(MessageProperties.getValue("message_signature")).toString();
        } else {
            text = new StringBuffer(form.getText()).append(form.getSignature()).toString();
        }
        MessageUtil.sendOne(form.getPhone().trim(), text.trim());
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
        String text;
        if (StringUtils.isEmpty(form.getSignature())) {
            text = new StringBuffer(form.getText()).append(MessageProperties.getValue("message_signature")).toString();
        } else {
            text = new StringBuffer(form.getText()).append(form.getSignature()).toString();
        }
        MessageUtil.sendGroup(form.getPhone().trim(), text.trim());
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
    public ResultData overview(String phone, String starttime, String endtime) {
        ResultData result = new ResultData();
        Map<String, Object> condition = new HashMap<>();
        if (!StringUtils.isEmpty(phone)) {
            condition.put("phone", phone);
        }
        if (!StringUtils.isEmpty(starttime)) {
            condition.put("startTime", starttime);
        }
        if (!StringUtils.isEmpty(endtime)) {
            condition.put("endTime", endtime);
        }
//        if (!StringUtils.isEmpty(pagesize)) {
//            condition.put("pagesize", pagesize);
//        }
//        if (!StringUtils.isEmpty(pageno)) {
//            condition.put("pageno", pageno);
//        }
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

    @GetMapping(value = "/template/list")
    public ResultData queryTemplate() {
        ResultData result = new ResultData();
        Map<String, Object> condition = new HashMap<>();
        condition.put("blockFlag", false);
        ResultData response = messageTemplateService.fetchTemplate(condition);
        if (response.getResponseCode() == ResponseCode.RESPONSE_ERROR) {
            result.setResponseCode(ResponseCode.RESPONSE_ERROR);
            result.setDescription("Fail to get message template.");
            return result;
        }
        if (response.getResponseCode() == ResponseCode.RESPONSE_NULL) {
            result.setResponseCode(ResponseCode.RESPONSE_NULL);
            result.setDescription("No message template found.");
            return result;
        }
        result.setResponseCode(ResponseCode.RESPONSE_OK);
        result.setData(response.getData());
        return result;
    }

    @RequestMapping(method = RequestMethod.POST, value = "/template/create")
    public ResultData createTemplate(MessageTemplateForm form) {
        ResultData result = new ResultData();
        if (StringUtils.isEmpty(form.getCatalog()) || StringUtils.isEmpty(form.getText())) {
            result.setResponseCode(ResponseCode.RESPONSE_ERROR);
            result.setDescription("Please ensure that all the required fields are filled with proper value");
            return result;
        }
        MessageTemplate template = new MessageTemplate(MessageCatalog.fromValue(form.getCatalog()), form.getText());
        ResultData response = messageTemplateService.createTemplate(template);
        if (response.getResponseCode() == ResponseCode.RESPONSE_OK) {
            result.setDescription(new StringBuffer("Template for catalog ").append(MessageCatalog.fromValue(form.getCatalog())).append(" has been created.").toString());
        } else {
            result.setResponseCode(ResponseCode.RESPONSE_ERROR);
            result.setDescription("Fail to create message template.");
        }
        return result;
    }
}
