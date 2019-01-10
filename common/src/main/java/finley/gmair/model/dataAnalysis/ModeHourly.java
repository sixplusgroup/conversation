package finley.gmair.model.dataAnalysis;

import finley.gmair.model.Entity;

public class ModeHourly extends Entity {
    private String statusId;
    private String machineId;
    private int manualMinute;
    private int cosyMinute;
    private int warmMinute;

    public ModeHourly() {
        super();
    }

    public ModeHourly(String machineId, int manualMinute, int cosyMinute, int warmMinute) {
        super();
        this.machineId = machineId;
        this.manualMinute = manualMinute;
        this.cosyMinute = cosyMinute;
        this.warmMinute = warmMinute;
    }

    public String getStatusId() {
        return statusId;
    }

    public void setStatusId(String statusId) {
        this.statusId = statusId;
    }

    public String getMachineId() {
        return machineId;
    }

    public void setMachineId(String machineId) {
        this.machineId = machineId;
    }

    public int getManualMinute() {
        return manualMinute;
    }

    public void setManualMinute(int manualMinute) {
        this.manualMinute = manualMinute;
    }

    public int getCosyMinute() {
        return cosyMinute;
    }

    public void setCosyMinute(int cosyMinute) {
        this.cosyMinute = cosyMinute;
    }

    public int getWarmMinute() {
        return warmMinute;
    }

    public void setWarmMinute(int warmMinute) {
        this.warmMinute = warmMinute;
    }
}
