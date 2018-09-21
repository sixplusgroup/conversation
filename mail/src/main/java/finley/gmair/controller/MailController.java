package finley.gmair.controller;

import finley.gmair.util.ResultData;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/mail")
public class MailController {

    @PostMapping("/send")
    public ResultData send() {
        ResultData result = new ResultData();

        return result;
    }
}
