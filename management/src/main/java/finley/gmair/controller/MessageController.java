package finley.gmair.controller;

import finley.gmair.service.MessageService;
import finley.gmair.util.ResultData;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

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

    @PostMapping("/send/group")
    public ResultData sendGroup(String phone,String text){
        return messageService.sendGroup(phone,text);
    }
}
