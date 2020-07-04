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
     * 设备唯一标识符
     */
    private String qrcode;

    /**
     * 设备是否需要清洗，最初默认为true
     */
    private boolean isNeedClean;

    /**
     * 设备初效滤网清洗提醒开关，最初默认为true
     */
    private boolean cleanRemindStatus;

    /**
     * 用户最近一次确认清洗的时间，最初默认为1970.01.01
     */
    private Date lastConfirmTime;

    public MachineFilterClean() {
        super();
    }

    public MachineFilterClean(String qrcode, boolean isNeedClean,
                              boolean cleanRemindStatus, Date lastConfirmTime) {
        this.qrcode = qrcode;
        this.isNeedClean = isNeedClean;
        this.cleanRemindStatus = cleanRemindStatus;
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

    public Date getLastConfirmTime() {
        return lastConfirmTime;
    }

    public void setLastConfirmTime(Date lastConfirmTime) {
        this.lastConfirmTime = lastConfirmTime;
    }
}
