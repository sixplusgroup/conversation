package finley.gmair.model.machine;

import org.springframework.data.mongodb.core.mapping.Document;

import java.io.Serializable;

@Document(collection = "machine_state_data")
public class MachineStateData extends Entity implements Serializable {
    private String uid;

    private int power;

    private int speed;

    private int mode;

    private int heat;

    private int led;

    private int light;

    private int childlock;

    public MachineStateData() {
        super();
    }

    public MachineStateData(String uid, int power, int speed, int mode, int heat, int led, int light, int childlock) {
        this();
        this.uid = uid;
        this.power = power;
        this.speed = speed;
        this.mode = mode;
        this.heat = heat;
        this.led = led;
        this.light = light;
        this.childlock = childlock;
    }

    public String getUid() {
        return uid;
    }

    public void setUid(String uid) {
        this.uid = uid;
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

    public int getMode() {
        return mode;
    }

    public void setMode(int mode) {
        this.mode = mode;
    }

    public int getHeat() {
        return heat;
    }

    public void setHeat(int heat) {
        this.heat = heat;
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

    public int getChildlock() {
        return childlock;
    }

    public void setChildlock(int childlock) {
        this.childlock = childlock;
    }
}
