package finley.gmair.model.mqtt;

import com.alibaba.fastjson.JSONObject;

public class AlertPayload {
    private String machineId;
    private int code;
    private String msg;

    public AlertPayload(String machineId, int code, String msg) {
        this.machineId = machineId;
        this.code = code;
        this.msg = msg;
    }

    public AlertPayload(String machineId, JSONObject object) {
        this(machineId, object.getIntValue("code"), object.getString("msg"));
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

    public String getMsg() {
        return msg;
    }

    public void setMsg(String msg) {
        this.msg = msg;
    }
}
