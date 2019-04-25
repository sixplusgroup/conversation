package finley.gmair.model.mqtt;

import com.alibaba.fastjson.JSONObject;

import java.sql.Timestamp;

public class StatusPayload {
    private String machineId;
    private String id;
    private Timestamp time;
    private int power;
    private int speed;
    private int heat;
    private int mode;
    private int childlock;
    private int led;
    private int light;

    public StatusPayload(String machineId, String id, Timestamp time, int power, int speed, int heat, int mode, int childlock, int led, int light) {
        this.machineId = machineId;
        this.id = id;
        this.time = time;
        this.power = power;
        this.speed = speed;
        this.heat = heat;
        this.mode = mode;
        this.childlock = childlock;
        this.led = led;
        this.light = light;
    }

    public StatusPayload(String machineId, JSONObject object) {
        this(machineId, object.getString("id"), new Timestamp(object.getLong("time")), object.getIntValue("power"),
                object.getIntValue("speed"), object.getIntValue("head"), object.getIntValue("mode"),
                object.getIntValue("childlock"), object.getIntValue("led"), object.getIntValue("light"));
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

    public int getSpeed() {
        return speed;
    }

    public void setSpeed(int speed) {
        this.speed = speed;
    }

    public int getHeat() {
        return heat;
    }

    public void setHeat(int heat) {
        this.heat = heat;
    }

    public int getMode() {
        return mode;
    }

    public void setMode(int mode) {
        this.mode = mode;
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

    public int getLight() {
        return light;
    }

    public void setLight(int light) {
        this.light = light;
    }
}
