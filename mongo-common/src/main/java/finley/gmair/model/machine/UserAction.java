package finley.gmair.model.machine;

import org.springframework.data.mongodb.core.mapping.Document;

@Document(collection = "user_machine_operation_log")
public class UserAction extends Entity{
    private String logId;
    private String userId;
    private String qrcode;
    private Long time;
    private String component;
    private String value;

    public UserAction(){super();}

    public UserAction(String logId, String userId, String qrcode, Long time, String component, String value){
        this();
        this.logId = logId;
        this.userId = userId;
        this.qrcode = qrcode;
        this.component = component;
        this.time = time;
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

    public Long getTime() {
        return time;
    }

    public void setTime(Long time) {
        this.time = time;
    }

    public String getComponent() {
        return component;
    }

    public void setComponent(String component) {
        this.component = component;
    }

    public String getValue() {
        return value;
    }

    public void setValue(String value) {
        this.value = value;
    }
}
