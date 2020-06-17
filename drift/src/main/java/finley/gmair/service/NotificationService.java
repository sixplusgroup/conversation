package finley.gmair.service;

import finley.gmair.model.drift.Notification;
import finley.gmair.util.ResultData;

import java.util.Map;

public interface NotificationService {
    ResultData createNotification(Notification notification);

    ResultData fetchNotification(Map<String, Object> condition);

    ResultData modifyNotification(Map<String, Object> condition);
}
