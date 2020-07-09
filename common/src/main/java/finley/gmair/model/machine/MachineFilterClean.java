package finley.gmair.model.machine;

import finley.gmair.model.Entity;

import java.util.Date;

/**
 * @author: Bright Chan
 * @date: 2020/7/4 11:58
 * @description: 设备初效滤网清洗提醒相关实体类
 */
public class MachineFilterClean extends Entity {

    /**
     * 设备二维码
     */
    private String qrcode;

    /**
     * 设备是否需要清洗，最初默认为false
     */
    private boolean isNeedClean;

    /**
     * 设备初效滤网清洗提醒开关，最初默认为true
     */
    private boolean cleanRemindStatus;

    /**
     * 是否在微信公众号中发送了推送提醒，最初默认为false
     * 每当isNeedClean属性由false变为true的时候，
     * isReminded属性就需要被重新赋值为false
     */
    private boolean isReminded;

    /**
     * 用户最近一次确认清洗的时间，初值为绑定时间
     */
    private Date lastConfirmTime;

    public MachineFilterClean() {
        super();
    }

    public MachineFilterClean(String qrcode, boolean isNeedClean, boolean cleanRemindStatus,
                              boolean isReminded, Date lastConfirmTime) {
        this.qrcode = qrcode;
        this.isNeedClean = isNeedClean;
        this.cleanRemindStatus = cleanRemindStatus;
        this.isReminded = isReminded;
        this.lastConfirmTime = lastConfirmTime;
    }

    public String getQrcode() {
        return qrcode;
    }

    public void setQrcode(String qrcode) {
        this.qrcode = qrcode;
    }

    public boolean isNeedClean() {
        return isNeedClean;
    }

    public void setNeedClean(boolean needClean) {
        isNeedClean = needClean;
    }

    public boolean isCleanRemindStatus() {
        return cleanRemindStatus;
    }

    public void setCleanRemindStatus(boolean cleanRemindStatus) {
        this.cleanRemindStatus = cleanRemindStatus;
    }

    public boolean isReminded() {
        return isReminded;
    }

    public void setReminded(boolean reminded) {
        isReminded = reminded;
    }

    public Date getLastConfirmTime() {
        return lastConfirmTime;
    }

    public void setLastConfirmTime(Date lastConfirmTime) {
        this.lastConfirmTime = lastConfirmTime;
    }
}
