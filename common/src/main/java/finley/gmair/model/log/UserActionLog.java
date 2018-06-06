package finley.gmair.model.log;

/**
 * @author shenghaohu@hshvip.com
 * @date 2018/6/6
 */
public class UserActionLog extends AbstractLog {
    private String logId;

    private String userId;

    private String machineValue;

    private long time;

    private String component;

    public UserActionLog(String userId, String machineValue, long time, String component, String logDetail, String ip) {
        super(logDetail, ip);
        this.userId = userId;
        this.machineValue = machineValue;
        this.time = time;
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

    public String getMachineValue() {
        return machineValue;
    }

    public void setMachineValue(String machineValue) {
        this.machineValue = machineValue;
    }

    public long getTime() {
        return time;
    }

    public void setTime(long time) {
        this.time = time;
    }

    public String getComponent() {
        return component;
    }

    public void setComponent(String component) {
        this.component = component;
    }
}