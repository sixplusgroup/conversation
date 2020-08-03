package finley.gmair.model.machine;

import finley.gmair.model.Entity;

/**
 * @author: Bright Chan
 * @date: 2020/7/25 15:24
 * @description: TODO
 */
public class MachineEfficientFilter extends Entity {

    /**
     * 设备二维码
     */
    private String qrcode;

    /**
     * 设备高效滤网使用状态（无需更换、需更换、急需更换）
     */
    private EfficientFilterStatus replaceStatus;

    /**
     * 更换提醒是否开启
     */
    private boolean replaceRemindOn;

    /**
     * 当前周期内，提醒的状态：
     * 0 - 一次没有提醒
     * 1 - 提醒过一次
     * 2 - 提醒过两次
     */
    private EfficientFilterRemindStatus isRemindedStatus;

    public MachineEfficientFilter() {
        super();
        this.replaceStatus = EfficientFilterStatus.NO_NEED;
        this.replaceRemindOn = true;
        this.isRemindedStatus = EfficientFilterRemindStatus.REMIND_ZERO;
    }

    public MachineEfficientFilter(String qrcode, EfficientFilterStatus replaceStatus,
                                  boolean replaceRemindOn, EfficientFilterRemindStatus isRemindedStatus) {
        super();
        this.qrcode = qrcode;
        this.replaceStatus = replaceStatus;
        this.replaceRemindOn = replaceRemindOn;
        this.isRemindedStatus = isRemindedStatus;
    }

    public String getQrcode() {
        return qrcode;
    }

    public void setQrcode(String qrcode) {
        this.qrcode = qrcode;
    }

    public EfficientFilterStatus getReplaceStatus() {
        return replaceStatus;
    }

    public void setReplaceStatus(EfficientFilterStatus replaceStatus) {
        this.replaceStatus = replaceStatus;
    }

    public boolean isReplaceRemindOn() {
        return replaceRemindOn;
    }

    public void setReplaceRemindOn(boolean replaceRemindOn) {
        this.replaceRemindOn = replaceRemindOn;
    }

    public EfficientFilterRemindStatus getIsRemindedStatus() {
        return isRemindedStatus;
    }

    public void setIsRemindedStatus(EfficientFilterRemindStatus isRemindedStatus) {
        this.isRemindedStatus = isRemindedStatus;
    }
}
