package finley.gmair.model.drift;

import finley.gmair.model.Entity;

/**
 * @ClassName: Notification
 * @Description: TODO
 * @Author fan
 * @Date 2019/7/31 1:28 PM
 */
public class Notification extends Entity {
    private String notificationId;

    private String activityId;

    private String context;

    public Notification(){super();}

    public Notification(String activityId, String context) {
        super();
        this.activityId = activityId;
        this.context = context;
    }

    public String getNotificationId() {
        return notificationId;
    }

    public void setNotificationId(String notificationId) {
        this.notificationId = notificationId;
    }

    public String getActivityId() {
        return activityId;
    }

    public void setActivityId(String activityId) {
        this.activityId = activityId;
    }

    public String getContext() {
        return context;
    }

    public void setContext(String context) {
        this.context = context;
    }
}
