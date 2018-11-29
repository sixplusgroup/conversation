package finley.gmair.model.machine;

import finley.gmair.model.Entity;

import java.sql.Timestamp;

public class Machine_on_off extends Entity {
    private String configId;

    private String machineId;

    private boolean status;

    private Timestamp startTime;

    private Timestamp endTime;

    public Machine_on_off() {
        super();
        this.status = true;
    }

    public Machine_on_off(String machineId, Timestamp startTime, Timestamp endTime) {
        this();
        this.machineId = machineId;
        this.startTime = startTime;
        this.endTime = endTime;
    }

    public String getConfigId() {
        return configId;
    }

    public void setConfigId(String configId) {
        this.configId = configId;
    }

    public String getMachineId() {
        return machineId;
    }

    public void setMachineId(String machineId) {
        this.machineId = machineId;
    }

    public boolean isStatus() {
        return status;
    }

    public void setStatus(boolean status) {
        this.status = status;
    }

    public Timestamp getStartTime() {
        return startTime;
    }

    public void setStartTime(Timestamp startTime) {
        this.startTime = startTime;
    }

    public Timestamp getEndTime() {
        return endTime;
    }

    public void setEndTime(Timestamp endTime) {
        this.endTime = endTime;
    }
}
