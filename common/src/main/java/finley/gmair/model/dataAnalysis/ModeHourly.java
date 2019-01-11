package finley.gmair.model.dataAnalysis;

import finley.gmair.model.Entity;

import java.sql.Timestamp;

public class ModeHourly extends Entity {
    private String statusId;
    private String machineId;
    private int autoMinute;
    private int manualMinute;
    private int sleepMinute;

    public ModeHourly() {
        super();
    }

    public ModeHourly(String machineId, int autoMinute, int manualMinute, int sleepMinute) {
        super();
        this.machineId = machineId;
        this.autoMinute = autoMinute;
        this.manualMinute = manualMinute;
        this.sleepMinute = sleepMinute;
    }
    public ModeHourly(String machineId, int autoMinute, int manualMinute, int sleepMinute, Timestamp timestamp) {
        super();
        this.machineId = machineId;
        this.autoMinute = autoMinute;
        this.manualMinute = manualMinute;
        this.sleepMinute = sleepMinute;
        this.createAt = timestamp;
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

    public int getAutoMinute() {
        return autoMinute;
    }

    public void setAutoMinute(int autoMinute) {
        this.autoMinute = autoMinute;
    }

    public int getManualMinute() {
        return manualMinute;
    }

    public void setManualMinute(int manualMinute) {
        this.manualMinute = manualMinute;
    }

    public int getSleepMinute() {
        return sleepMinute;
    }

    public void setSleepMinute(int sleepMinute) {
        this.sleepMinute = sleepMinute;
    }
}
