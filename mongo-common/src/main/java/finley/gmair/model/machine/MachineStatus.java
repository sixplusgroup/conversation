package finley.gmair.model.machine;

import finley.gmair.model.Entity;

public class MachineStatus extends Entity {
    private String uid;

    private int pm2_5;

    private int temp;

    private int humid;

    private int hcho;

    private int co2;

    private int volume;

    private int power;

    private int mode;

    public MachineStatus() {
        super();
    }

    public MachineStatus(String uid, int pm2_5, int temp, int humid, int hcho, int co2, int volume, int power, int mode) {
        this();
        this.uid = uid;
        this.pm2_5 = pm2_5;
        this.temp = temp;
        this.humid = humid;
        this.hcho = hcho;
        this.co2 = co2;
        this.volume = volume;
        this.power = power;
        this.mode = mode;
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

    public int getHcho() {
        return hcho;
    }

    public void setHcho(int hcho) {
        this.hcho = hcho;
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
}
