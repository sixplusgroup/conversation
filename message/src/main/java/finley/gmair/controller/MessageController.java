package finley.gmair.controller;

import finley.gmair.model.message.TextMessage;
import finley.gmair.util.ResponseCode;
import finley.gmair.util.ResultData;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RequestMapping("/message")
@RestController
public class MessageController {

    @GetMapping("/receive")
    public ResultData receive(String message, String mobile) {
        ResultData result = new ResultData();
        if (StringUtils.isEmpty(message) || StringUtils.isEmpty(mobile)) {
            result.setResponseCode(ResponseCode.RESPONSE_ERROR);
            return result;
        }
        TextMessage in = new TextMessage(mobile, message);
        return result;
    }
}
