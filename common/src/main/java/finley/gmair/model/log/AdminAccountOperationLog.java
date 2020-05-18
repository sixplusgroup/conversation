package finley.gmair.model.log;

public class AdminAccountOperationLog extends AbstractLog {

    private String logId;

    private String userId;

    private String component;

    public AdminAccountOperationLog(String detail, String ip, String userId, String component) {
        super(detail, ip);
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
