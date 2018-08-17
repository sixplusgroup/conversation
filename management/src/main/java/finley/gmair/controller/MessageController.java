package finley.gmair.controller;

import finley.gmair.service.MessageService;
import finley.gmair.util.ResultData;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/management/message")
public class MessageController {

    @Autowired
    private MessageService messageService;

    @PostMapping("/reconnaissance/send")
    public ResultData sendMessage(String phone) {
        String text = "[果麦新风]勘测完成提醒";
        return messageService.sendMessage(phone, text);
    }

    @GetMapping("/template/list")
    public ResultData getMessage(){
        return messageService.findMessageTemplate();
    }
}
