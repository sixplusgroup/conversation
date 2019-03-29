package finley.gmair.model.mqtt;

import com.alibaba.fastjson.JSONObject;

import java.sql.Timestamp;

public class StatusPayload {
    private String machineId;
    private String id;
    private Timestamp time;
    private int power;
    private int level;
    private int ptc;
    private int mode;
    private int newwind;
    private int backwind;
    private int childlock;
    private int led;

    public StatusPayload(String machineId, String id, Timestamp time, int power, int level, int ptc, int mode, int newwind, int backwind, int childlock, int led) {
        this.machineId = machineId;
        this.id = id;
        this.time = time;
        this.power = power;
        this.level = level;
        this.ptc = ptc;
        this.mode = mode;
        this.newwind = newwind;
        this.backwind = backwind;
        this.childlock = childlock;
        this.led = led;
    }

    public StatusPayload(String machineId, JSONObject object) {
        this(machineId, object.getString("id"), new Timestamp(object.getLong("time")), object.getIntValue("power"),
                object.getIntValue("level"), object.getIntValue("ptc"), object.getIntValue("mode"),
                object.getIntValue("newwind"), object.getIntValue("backwind"), object.getIntValue("childlock"),
                object.getIntValue("led"));
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

    public int getPower() {
        return power;
    }

    public void setPower(int power) {
        this.power = power;
    }

    public int getLevel() {
        return level;
    }

    public void setLevel(int level) {
        this.level = level;
    }

    public int getPtc() {
        return ptc;
    }

    public void setPtc(int ptc) {
        this.ptc = ptc;
    }

    public int getMode() {
        return mode;
    }

    public void setMode(int mode) {
        this.mode = mode;
    }

    public int getNewwind() {
        return newwind;
    }

    public void setNewwind(int newwind) {
        this.newwind = newwind;
    }

    public int getBackwind() {
        return backwind;
    }

    public void setBackwind(int backwind) {
        this.backwind = backwind;
    }

    public int getChildlock() {
        return childlock;
    }

    public void setChildlock(int childlock) {
        this.childlock = childlock;
    }

    public int getLed() {
        return led;
    }

    public void setLed(int led) {
        this.led = led;
    }
}
