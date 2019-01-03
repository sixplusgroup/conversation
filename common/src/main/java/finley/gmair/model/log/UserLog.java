package finley.gmair.model.log;

public class UserLog extends AbstractLog {

    private String logId;

    private String userId;

    private String component;

    public UserLog(String logDetail, String ip, String userId, String component) {
        super(logDetail, ip);
        this.userId = userId;
        this.component = component;
    }

    public String getLogId() {
        return logId;
    }

    public void setLogId(String logId) {
        this.logId = logId;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public String getComponent() {
        return component;
    }

    public void setComponent(String component) {
        this.component = component;
    }
}
