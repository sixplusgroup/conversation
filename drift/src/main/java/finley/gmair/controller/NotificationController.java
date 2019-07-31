package finley.gmair.controller;

import finley.gmair.util.ResultData;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * @ClassName: NotificationController
 * @Description: Responsible for notification related jobs
 * @Author fan
 * @Date 2019/7/31 1:32 PM
 */
@RestController
@RequestMapping("/drift/notification")
public class NotificationController {
    private Logger logger = LoggerFactory.getLogger(NotificationController.class);

    @PostMapping("/create")
    public ResultData create(String activityId, String context) {
        ResultData result = new ResultData();

        return result;
    }

    @GetMapping("/list")
    public ResultData list() {
        ResultData result = new ResultData();

        return result;
    }
}
