package finley.gmair.model.machine;

import java.io.Serializable;

/**
 * @author: Bright Chan
 * @date: 2020/8/6 12:39
 * @description: 设备控制指令的对象型参数
 */
public class ConfigOption implements Serializable {

    private String uid;

    private Integer power;

    private Integer speed;

    private Integer heat;

    private Integer mode;

    private Integer childlock;

    private Integer light;

    private Integer turbo;

    private Integer panel;

    public ConfigOption() {
    }

    public ConfigOption(String uid, Integer power, Integer speed, Integer heat, Integer mode,
                        Integer childlock, Integer light, Integer turbo, Integer panel) {
        this.uid = uid;
        this.power = power;
        this.speed = speed;
        this.heat = heat;
        this.mode = mode;
        this.childlock = childlock;
        this.light = light;
        this.turbo = turbo;
        this.panel = panel;
    }

    public String getUid() {
        return uid;
    }

    public void setUid(String uid) {
        this.uid = uid;
    }

    public Integer getPower() {
        return power;
    }

    public void setPower(Integer power) {
        this.power = power;
    }

    public Integer getSpeed() {
        return speed;
    }

    public void setSpeed(Integer speed) {
        this.speed = speed;
    }

    public Integer getHeat() {
        return heat;
    }

    public void setHeat(Integer heat) {
        this.heat = heat;
    }

    public Integer getMode() {
        return mode;
    }

    public void setMode(Integer mode) {
        this.mode = mode;
    }

    public Integer getChildlock() {
        return childlock;
    }

    public void setChildlock(Integer childlock) {
        this.childlock = childlock;
    }

    public Integer getLight() {
        return light;
    }

    public void setLight(Integer light) {
        this.light = light;
    }

    public Integer getTurbo() {
        return turbo;
    }

    public void setTurbo(Integer turbo) {
        this.turbo = turbo;
    }

    public Integer getPanel() {
        return panel;
    }

    public void setPanel(Integer panel) {
        this.panel = panel;
    }
}
