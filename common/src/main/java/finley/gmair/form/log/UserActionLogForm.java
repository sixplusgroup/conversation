package finley.gmair.form.log;

/**
 * @author shenghaohu@hshvip.com
 * @date 2018/6/6
 */
public class UserActionLogForm {
    private String userId;

    private String machineValue;

    private String component;

    private String logDetail;

    private String ip;

    private Object actionValue;

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

    public String getComponent() {
        return component;
    }

    public void setComponent(String component) {
        this.component = component;
    }

    public String getLogDetail() {
        return logDetail;
    }

    public void setLogDetail(String logDetail) {
        this.logDetail = logDetail;
    }

    public String getIp() {
        return ip;
    }

    public void setIp(String ip) {
        this.ip = ip;
    }

    public Object getActionValue() {
        return actionValue;
    }

    public void setActionValue(Object actionValue) {
        this.actionValue = actionValue;
    }
}