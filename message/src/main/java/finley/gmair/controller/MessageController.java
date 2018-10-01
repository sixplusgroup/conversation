package finley.gmair.controller;

import finley.gmair.model.message.TextMessage;
import finley.gmair.service.MessageService;
import finley.gmair.util.ResponseCode;
import finley.gmair.util.ResultData;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.HashMap;
import java.util.Map;

@RequestMapping("/message")
@RestController
public class MessageController {

    @Autowired
    private MessageService messageService;

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

    @GetMapping("/receive/list")
    public ResultData list() {
        ResultData result = new ResultData();
        Map<String, Object> condition = new HashMap<>();
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
}
