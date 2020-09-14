package finley.gmair.model.machine;

import finley.gmair.model.Entity;

import java.util.Date;

/**
 * @author: ck
 * @date: 2020/9/14 15:24
 * @description: TODO
 */
public class MachineEfficientInformation extends Entity {

    /**
     * 设备二维码
     */
    private String qrcode;

    /**
     * 用户最近一次确认清洗的时间，初值为绑定时间
     */
    private Date lastConfirmTime;

    /**
     * 设备在线的运行时间
     */
    private int running;

    /**
     * PM2.5B（舱内）数值超过25的连续天数（420）
     */
    private int conti;

    /**
     * 设备所处城市PM2.5在当前周期内大于75的天数
     */
    private int abnormal;

    public MachineEfficientInformation(String qrcode, Date lastConfirmTime, int running, int conti, int abnormal) {
        super();
        this.qrcode = qrcode;
        this.lastConfirmTime = lastConfirmTime;
        this.running = running;
        this.conti = conti;
        this.abnormal = abnormal;
    }

    public String getQrcode() {
        return qrcode;
    }

    public void setQrcode(String qrcode) {
        this.qrcode = qrcode;
    }

    public Date getLastConfirmTime() {
        return lastConfirmTime;
    }

    public void setLastConfirmTime(Date lastConfirmTime) {
        this.lastConfirmTime = lastConfirmTime;
    }

    public int getRunning() {
        return running;
    }

    public void setRunning(int running) {
        this.running = running;
    }

    public int getConti() {
        return conti;
    }

    public void setConti(int conti) {
        this.conti = conti;
    }

    public int getAbnormal() {
        return abnormal;
    }

    public void setAbnormal(int abnormal) {
        this.abnormal = abnormal;
    }
}
