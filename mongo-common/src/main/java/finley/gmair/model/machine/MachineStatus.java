package finley.gmair.model.machine;

import org.springframework.data.mongodb.core.mapping.Document;

@Document(collection = "machine_status")
public class MachineStatus extends Entity {
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

    private int lock;

    public MachineStatus() {
        super();
    }

    public MachineStatus(String uid, int pm2_5, int temp, int humid, int co2, int volume, int power, int mode, int heat, int light, int lock) {
        this();
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
        this.lock = lock;
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

    public int getLock() {
        return lock;
    }

    public void setLock(int lock) {
        this.lock = lock;
    }
}
