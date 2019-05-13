package finley.gmair.model.machine.v3;

import finley.gmair.model.Entity;
import finley.gmair.model.machine.v2.PowerStatus;
import finley.gmair.model.machine.v2.WorkMode;

/**
 * @ClassName: MachineStatusV3
 * @Description: TODO
 * @Author fan
 * @Date 2019/5/11 7:53 PM
 */
public class MachineStatusV3 extends Entity {
    private String uid;

    private int pm2_5a;

    private int pm2_5b;

    private int tempIndoor;

    private int tempOutdoor;

    private int humidity;

    private int co2;

    private PowerStatus status;

    private WorkMode mode;

    private int volume;

    private int heat;

    private int light;

    public String getUid() {
        return uid;
    }

    public void setUid(String uid) {
        this.uid = uid;
    }

    public int getPm2_5a() {
        return pm2_5a;
    }

    public void setPm2_5a(int pm2_5a) {
        this.pm2_5a = pm2_5a;
    }

    public int getPm2_5b() {
        return pm2_5b;
    }

    public void setPm2_5b(int pm2_5b) {
        this.pm2_5b = pm2_5b;
    }

    public int getTempIndoor() {
        return tempIndoor;
    }

    public void setTempIndoor(int tempIndoor) {
        this.tempIndoor = tempIndoor;
    }

    public int getTempOutdoor() {
        return tempOutdoor;
    }

    public void setTempOutdoor(int tempOutdoor) {
        this.tempOutdoor = tempOutdoor;
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

    public PowerStatus getStatus() {
        return status;
    }

    public void setStatus(PowerStatus status) {
        this.status = status;
    }

    public WorkMode getMode() {
        return mode;
    }

    public void setMode(WorkMode mode) {
        this.mode = mode;
    }

    public int getVolume() {
        return volume;
    }

    public void setVolume(int volume) {
        this.volume = volume;
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
