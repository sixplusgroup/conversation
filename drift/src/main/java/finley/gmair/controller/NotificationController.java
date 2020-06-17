package finley.gmair.controller;

import finley.gmair.model.drift.Activity;
import finley.gmair.model.drift.Notification;
import finley.gmair.service.ActivityService;
import finley.gmair.service.NotificationService;
import finley.gmair.util.ResponseCode;
import finley.gmair.util.ResultData;
import finley.gmair.util.StringUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

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

    @Autowired
    private ActivityService activityService;

    @Autowired
    private NotificationService notificationService;

    @PostMapping("/create")
    public ResultData create(String activityId, String context) {
        ResultData result = new ResultData();
        // 检查参数的完整性
        if (StringUtil.isEmpty(activityId,context)){
            result.setResponseCode(ResponseCode.RESPONSE_ERROR);
            result.setDescription("请确认活动名称和通知内容的完整性");
            return result;
        }
        //check activityId
        Map<String, Object> condition = new HashMap<>();
        if (!StringUtils.isEmpty(activityId)) {
            condition.put("activityId", activityId);
        }
        condition.put("blockFlag", false);
        ResultData trycon = activityService.fetchActivity(condition);
        if (trycon.getResponseCode() == ResponseCode.RESPONSE_NULL) {
            result.setResponseCode(ResponseCode.RESPONSE_NULL);
            result.setDescription("No activity found from database by activityId");
            return result;
        }
        if (trycon.getResponseCode() == ResponseCode.RESPONSE_ERROR) {
            result.setResponseCode(ResponseCode.RESPONSE_ERROR);
            result.setDescription("Query activity error, please try again later");
            return result;
        }
        //build notification entity
        Notification notification = new Notification(activityId,context);
        ResultData response = notificationService.createNotification(notification);
        if (response.getResponseCode() == ResponseCode.RESPONSE_ERROR) {
            result.setResponseCode(ResponseCode.RESPONSE_ERROR);
            result.setDescription("Fail to store notification to database");
            return result;
        }
        if (response.getResponseCode() == ResponseCode.RESPONSE_OK) {
            result.setResponseCode(ResponseCode.RESPONSE_OK);
            result.setData(response.getData());
        }
        return result;
    }

    @GetMapping("/list")
    public ResultData list() {
        ResultData result = new ResultData();
        Map<String, Object> condition = new HashMap<>();
        condition.put("blockFlag", false);
        ResultData response = notificationService.fetchNotification(condition);
        if (response.getResponseCode() == ResponseCode.RESPONSE_ERROR) {
            result.setResponseCode(ResponseCode.RESPONSE_ERROR);
            result.setDescription("Fail to get notification by activityId");
        } else if (response.getResponseCode() == ResponseCode.RESPONSE_NULL) {
            result.setResponseCode(ResponseCode.RESPONSE_NULL);
            result.setDescription("No notification found by activityId");
        } else {
            result.setResponseCode(ResponseCode.RESPONSE_OK);
            result.setData(response.getData());
        }
        return result;
    }
}
