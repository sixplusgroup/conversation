package finley.gmair.model.log;

/**
 * @author shenghaohu@hshvip.com
 * @date 2018/6/6
 */
public class UserMachineOperationLog extends AbstractLog {
    private String logId;

    private String userId;

    private String qrcode;

    private long time;

    private String component;

    private String value;

    public UserMachineOperationLog(String userId, String qrcode, long time, String component, String detail, String ip, String value) {
        super(detail, ip);
        this.userId = userId;
        this.qrcode = qrcode;
        this.time = time;
        this.component = component;
        this.value = value;
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

    public String getQrcode() {
        return qrcode;
    }

    public void setQrcode(String qrcode) {
        this.qrcode = qrcode;
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

    public Object getValue() {
        return value;
    }

    public void setValue(String value) {
        this.value = value;
    }
}