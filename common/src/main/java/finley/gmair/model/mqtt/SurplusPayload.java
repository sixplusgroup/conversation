package finley.gmair.model.mqtt;

import com.alibaba.fastjson.JSONObject;

import java.sql.Timestamp;

public class SurplusPayload {
    private String machineId;
    private String id;
    private Timestamp time;
    private int bottom;
    private int middle;
    private int top;

    public SurplusPayload(String machineId, String id, Timestamp time, int bottom, int middle, int top) {
        this.machineId = machineId;
        this.id = id;
        this.time = time;
        this.bottom = bottom;
        this.middle = middle;
        this.top = top;
    }

    public SurplusPayload(String machineId, JSONObject object) {
        this(machineId, object.getString("id"), new Timestamp(object.getLong("time")),
                object.getIntValue("1"), object.getIntValue("2"), object.getIntValue("3"));
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

    public int getBottom() {
        return bottom;
    }

    public void setBottom(int bottom) {
        this.bottom = bottom;
    }

    public int getMiddle() {
        return middle;
    }

    public void setMiddle(int middle) {
        this.middle = middle;
    }

    public int getTop() {
        return top;
    }

    public void setTop(int top) {
        this.top = top;
    }
}
