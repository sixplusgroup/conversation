package finley.gmair.model.log;

public class MqttAckLog extends AbstractLog {
    private String logId;
    private String ackId;
    private String machineId;
    private int code;
    private String component;

    public MqttAckLog(String detail, String ip, String ackId, String machineId, int code, String component) {
        super(detail, ip);
        this.ackId = ackId;
        this.machineId = machineId;
        this.code = code;
        this.component = component;
    }

    public String getLogId() {
        return logId;
    }

    public void setLogId(String logId) {
        this.logId = logId;
    }

    public String getAckId() {
        return ackId;
    }

    public void setAckId(String ackId) {
        this.ackId = ackId;
    }

    public String getMachineId() {
        return machineId;
    }

    public void setMachineId(String machineId) {
        this.machineId = machineId;
    }

    public int getCode() {
        return code;
    }

    public void setCode(int code) {
        this.code = code;
    }

    public String getComponent() {
        return component;
    }

    public void setComponent(String component) {
        this.component = component;
    }
}
