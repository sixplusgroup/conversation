package finley.gmair.model.mqtt;

import com.alibaba.fastjson.JSONObject;

import java.sql.Timestamp;

public class SurplusPayload {
    private String machineId;
    private String id;
    private Timestamp time;
    private int remain;

    public SurplusPayload(String machineId, String id, Timestamp time, int remain) {
        this.machineId = machineId;
        this.id = id;
        this.time = time;
        this.remain = remain;
    }

    public SurplusPayload(String machineId, JSONObject object) {
        this(machineId, object.getString("id"), new Timestamp(object.getLong("time")),
                object.getIntValue("remain"));
    }

    public String getMachineId() {
        return machineId;
    }

    public void setMachineId(String machineId) {
        this.machineId = machineId;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public Timestamp getTime() {
        return time;
    }

    public void setTime(Timestamp time) {
        this.time = time;
    }

    public int getRemain() {
        return remain;
    }

    public void setRemain(int remain) {
        this.remain = remain;
    }
}
