package finley.gmair.controller;

import finley.gmair.model.message.MessageCatalog;
import finley.gmair.model.message.TextMessage;
import finley.gmair.service.MessageService;
import finley.gmair.service.MessageTemplateService;
import finley.gmair.util.ResponseCode;
import finley.gmair.util.ResultData;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.Map;

@RequestMapping("/message")
@RestController
public class MessageController {

    @Autowired
    private MessageService messageService;

    @Autowired
    private MessageTemplateService messageTemplateService;

    @GetMapping("/receive")
    public ResultData receive(String message, String mobile) {
        ResultData result = new ResultData();
        if (StringUtils.isEmpty(message) || StringUtils.isEmpty(mobile)) {
            result.setResponseCode(ResponseCode.RESPONSE_ERROR);
            return result;
        }
        TextMessage in = new TextMessage(mobile, message);
        ResultData response = messageService.createReceiveMessage(in);
        if (response.getResponseCode() == ResponseCode.RESPONSE_ERROR) {
            result.setResponseCode(ResponseCode.RESPONSE_ERROR);
            result.setDescription("Fail to save uploaded message to database");
            return result;
        }
        result.setResponseCode(ResponseCode.RESPONSE_OK);
        result.setData(response.getData());
        return result;
    }

    /**
     * 接收短信列表
     *
     * @return
     */
    @GetMapping("/receive/list")
    public ResultData list(String phone, String starttime, String endtime) {
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
        condition.put("blockFlag", false);
        ResultData response = messageService.fetchReceiveMessage(condition);
        if (response.getResponseCode() == ResponseCode.RESPONSE_OK) {
            result.setResponseCode(ResponseCode.RESPONSE_OK);
            result.setData(response.getData());
        }
        if (response.getResponseCode() == ResponseCode.RESPONSE_NULL) {
            result.setResponseCode(ResponseCode.RESPONSE_NULL);
            result.setDescription("No received message found");
        }
        if (response.getResponseCode() == ResponseCode.RESPONSE_ERROR) {
            result.setResponseCode(ResponseCode.RESPONSE_ERROR);
            result.setDescription("Fail to retrieve received message list");
        }
        return result;
    }

    /**
     * @return
     */
    @GetMapping("/template/{key}")
    public ResultData template(@PathVariable("key") String key) {
        ResultData result = new ResultData();
        Map<String, Object> condition = new HashMap<>();
        switch (key.toUpperCase()) {
            case "REGISTRATION":
                condition.put("catalog", MessageCatalog.REGISTRATION.getCode());
                break;
            case "AUTHENTICATION":
                condition.put("catalog", MessageCatalog.AUTHENTICATION.getCode());
                break;
            case "NOTIFICATION_DISPATCHED":
                condition.put("catalog", MessageCatalog.NOTIFICATION_DISPATCHED.getCode());
                break;
            case "NOTIFICATION_INSTALL":
                condition.put("catalog", MessageCatalog.NOTIFICATION_INSTALL.getCode());
                break;
            default:
                result.setResponseCode(ResponseCode.RESPONSE_ERROR);
                result.setDescription(new StringBuffer("Key: ").append(key).append(" not listed").toString());
                return result;
        }
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
        result.setData(response.getData());
        return result;
    }
}
