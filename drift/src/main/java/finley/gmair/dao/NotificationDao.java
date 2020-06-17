package finley.gmair.dao;

import finley.gmair.model.drift.Notification;
import finley.gmair.util.ResultData;

import java.util.Map;

public interface NotificationDao {
    ResultData insert(Notification notification);

    ResultData query(Map<String, Object> condition);

    ResultData update(Map<String, Object> condition);
}
