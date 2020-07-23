package finley.gmair.model.machine;

import finley.gmair.model.Entity;

/**
 * @author: Bright Chan
 * @date: 2020/7/18 15:49
 * @description: TODO
 */
public class MachineTurboVolume extends Entity {

    /**
     * 设备二维码
     */
    private String qrcode;

    /**
     * 设备隐藏风量开关
     */
    private boolean turboVolumeStatus;

    public MachineTurboVolume() {
        super();
    }

    public MachineTurboVolume(String qrcode, boolean turboVolumeStatus) {
        super();
        this.qrcode = qrcode;
        this.turboVolumeStatus = turboVolumeStatus;
    }

    public String getQrcode() {
        return qrcode;
    }

    public void setQrcode(String qrcode) {
        this.qrcode = qrcode;
    }

    public boolean isTurboVolumeStatus() {
        return turboVolumeStatus;
    }

    public void setTurboVolumeStatus(boolean turboVolumeStatus) {
        this.turboVolumeStatus = turboVolumeStatus;
    }
}
