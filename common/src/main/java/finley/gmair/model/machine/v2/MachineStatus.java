package finley.gmair.model.machine.v2;

import finley.gmair.model.Entity;

public class MachineStatus extends Entity {
    
    private static final long serialVersionUID = 1L;

    private String uid;

    private int pm2_5;

    private int temperature;

    private int humidity;

    private int co2;

    private int volume;

    private PowerStatus pStatus;

    //work mode to be decided
    private WorkMode mode;

    private HeatStatus hStatus;

    private boolean lock;

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

    public int getTemperature() {
        return temperature;
    }

    public void setTemperature(int temperature) {
        this.temperature = temperature;
    }

    public int getHumidity() {
        return humidity;
    }

    public void setHumidity(int humidity) {
        this.humidity = humidity;
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

    public PowerStatus getpStatus() {
        return pStatus;
    }

    public void setpStatus(PowerStatus pStatus) {
        this.pStatus = pStatus;
    }

    public WorkMode getMode() {
        return mode;
    }

    public void setMode(WorkMode mode) {
        this.mode = mode;
    }

    public HeatStatus gethStatus() {
        return hStatus;
    }

    public void sethStatus(HeatStatus hStatus) {
        this.hStatus = hStatus;
    }

    public boolean isLock() {
        return lock;
    }

    public void setLock(boolean lock) {
        this.lock = lock;
    }
}
