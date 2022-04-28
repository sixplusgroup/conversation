package finley.gmair.model.log;

/**
 * @author shenghaohu@hshvip.com
 * @date 2018/6/6
 */
public class Server2MachineLog extends AbstractLog {
    private String logId;

    private String machineValue;

    private String component;

    private long time;

    public Server2MachineLog(String machineValue, String component, long time, String logDetail, String ip) {
        super(logDetail, ip);
        this.machineValue = machineValue;
        this.component = component;
        this.time = time;
    }

    public String getLogId() {
        return logId;
    }

    public void setLogId(String logId) {
        this.logId = logId;
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

    public long getTime() {
        return time;
    }

    public void setTime(long time) {
        this.time = time;
    }
}