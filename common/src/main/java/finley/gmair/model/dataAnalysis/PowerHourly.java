package finley.gmair.model.dataAnalysis;

import finley.gmair.model.Entity;

public class PowerHourly extends Entity {

    private String statusId;
    private String machineId;
    private int powerOnMinute;
    private int powerOffMinute;

    public PowerHourly() {
        super();
    }

    public PowerHourly(String machineId, int powerOnMinute, int powerOffMinute) {
        super();
        this.machineId = machineId;
        this.powerOnMinute = powerOnMinute;
        this.powerOffMinute = powerOffMinute;
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

    public int getPowerOnMinute() {
        return powerOnMinute;
    }

    public void setPowerOnMinute(int powerOnMinute) {
        this.powerOnMinute = powerOnMinute;
    }

    public int getPowerOffMinute() {
        return powerOffMinute;
    }

    public void setPowerOffMinute(int powerOffMinute) {
        this.powerOffMinute = powerOffMinute;
    }
}
