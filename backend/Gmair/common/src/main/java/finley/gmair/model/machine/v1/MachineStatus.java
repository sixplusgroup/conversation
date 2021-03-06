package finley.gmair.model.machine.v1;

import finley.gmair.model.Entity;

import java.io.Serializable;

public class MachineStatus extends Entity implements Serializable {

    private static final long serialVersionUID = 1L;

    private String uid;

    /*length = 2*/
    private int pm2_5;

    /*length = 1*/
    private int temp;

    /*length = 1*/
    private int humid;

    /*length = 2*/
    private int co2;

    /*length = 2*/
    private int volume;

    /*length = 1*/
    private int power;

    /*length = 1*/
    private int mode;

    /*length = 1*/
    private int heat;

    /*light = 1*/
    private int light;

    public MachineStatus() {
        super();
    }

    public MachineStatus(String uid, int pm2_5, int temp, int humid, int co2, int volume, int power, int mode, int heat, int light) {
        super();
        this.uid = uid;
        this.pm2_5 = pm2_5;
        this.temp = temp;
        this.humid = humid;
        this.co2 = co2;
        this.volume = volume;
        this.power = power;
        this.mode = mode;
        this.heat = heat;
        this.light = light;
    }

    public String getUid() {
        return uid;
    }

    public void setUid(String uid) {
        this.uid = uid;
    }

    public int getPm2_5() {
        return pm2_5;
    }

    public void setPm2_5(int pm2_5) {
        this.pm2_5 = pm2_5;
    }

    public int getTemp() {
        return temp;
    }

    public void setTemp(int temp) {
        this.temp = temp;
    }

    public int getHumid() {
        return humid;
    }

    public void setHumid(int humid) {
        this.humid = humid;
    }

    public int getCo2() {
        return co2;
    }

    public void setCo2(int co2) {
        this.co2 = co2;
    }

    public int getVolume() {
        return volume;
    }

    public void setVolume(int volume) {
        this.volume = volume;
    }

    public int getPower() {
        return power;
    }

    public void setPower(int power) {
        this.power = power;
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

    public int getLight() {
        return light;
    }

    public void setLight(int light) {
        this.light = light;
    }
}
