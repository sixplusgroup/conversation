package finley.gmair.model.fan;

import finley.gmair.model.Entity;

import java.io.Serializable;

/**
 * @ClassName: FanStatus
 * @Description: TODO
 * @Author fan
 * @Date 2019/11/13 7:42 PM
 */
public class FanStatus extends Entity implements Serializable {
  
    private static final long serialVersionUID = 1L;

    private String mac;

    private int power;

    private int speed;

    private int mode;

    private int sweep;

    private int heat;

    private int runtime;

    private int countdown;

    private int targettemp;

    private int temp;

    private int mutemode;

    public FanStatus() {
        super();
    }

    public FanStatus(String mac, int power, int speed, int mode, int sweep, int heat, int runtime, int countdown, int targettemp, int temp,int mutemode) {
        this();
        this.mac = mac;
        this.power = power;
        this.speed = speed;
        this.mode = mode;
        this.sweep = sweep;
        this.heat = heat;
        this.runtime = runtime;
        this.countdown = countdown;
        this.targettemp = targettemp;
        this.temp = temp;
        this.mutemode = mutemode;
    }

    public String getMac() {
        return mac;
    }

    public void setMac(String mac) {
        this.mac = mac;
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

    public int getSweep() {
        return sweep;
    }

    public void setSweep(int sweep) {
        this.sweep = sweep;
    }

    public int getHeat() {
        return heat;
    }

    public void setHeat(int heat) {
        this.heat = heat;
    }

    public int getRuntime() {
        return runtime;
    }

    public void setRuntime(int runtime) {
        this.runtime = runtime;
    }

    public int getCountdown() {
        return countdown;
    }

    public void setCountdown(int countdown) {
        this.countdown = countdown;
    }

    public int getTargettemp() {
        return targettemp;
    }

    public void setTargettemp(int targettemp) {
        this.targettemp = targettemp;
    }

    public int getTemp() {
        return temp;
    }

    public void setTemp(int temp) {
        this.temp = temp;
    }

    public int getMutemode() {
        return mutemode;
    }

    public void setMutemode(int mutemode) {
        this.mutemode = mutemode;
    }
}
