package finley.gmair.model.mqtt;

import com.alibaba.fastjson.JSONObject;

public class AckPayload {
    private String machineId;
    private String ackId;
    private int code;
    private String error;

    public AckPayload(String machineId, String ackId, int code) {
        this.machineId = machineId;
        this.ackId = ackId;
        this.code = code;
    }

    public AckPayload(String machineId, JSONObject object) {
        this(machineId, object.getString("ack_id"), object.getIntValue("code"));
    }

    public String getMachineId() {
        return machineId;
    }

    public void setMachineId(String machineId) {
        this.machineId = machineId;
    }

    public String getAckId() {
        return ackId;
    }

    public void setAckId(String ackId) {
        this.ackId = ackId;
    }

    public int getCode() {
        return code;
    }

    public void setCode(int code) {
        this.code = code;
    }

    public String getError() {
        return error;
    }

    public void setError(String error) {
        this.error = error;
    }
}
