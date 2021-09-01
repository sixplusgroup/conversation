package finley.gmair.model.mqttManagement;

import finley.gmair.model.Entity;

/**
 * 设备警报
 *
 * @author lycheeshell
 * @date 2020/12/3 22:23
 */
public class MachineAlert extends Entity {
    private String alertId;
    private String machineId;
    private int alertCode;
    private String alertMsg;
    private boolean alertStatus;

    public MachineAlert() {
        super();
        this.alertStatus = false;
    }

    public MachineAlert(String machineId, int alertCode, String alertMsg) {
        this();
        this.machineId = machineId;
        this.alertCode = alertCode;
        this.alertMsg = alertMsg;
    }

    public String getAlertId() {
        return alertId;
    }

    public void setAlertId(String alertId) {
        this.alertId = alertId;
    }

    public String getMachineId() {
        return machineId;
    }

    public void setMachineId(String machineId) {
        this.machineId = machineId;
    }

    public int getAlertCode() {
        return alertCode;
    }

    public void setAlertCode(int alertCode) {
        this.alertCode = alertCode;
    }

    public String getAlertMsg() {
        return alertMsg;
    }

    public void setAlertMsg(String alertMsg) {
        this.alertMsg = alertMsg;
    }

    public boolean isAlertStatus() {
        return alertStatus;
    }

    public void setAlertStatus(boolean alertStatus) {
        this.alertStatus = alertStatus;
    }
}
