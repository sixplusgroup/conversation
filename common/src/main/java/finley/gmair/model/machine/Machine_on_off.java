package finley.gmair.model.machine;

import finley.gmair.model.Entity;

import java.time.LocalTime;

public class Machine_on_off extends Entity {
    private String configId;

    private String machineId;

    private boolean status;

    private LocalTime startTime;

    private LocalTime endTime;

    public Machine_on_off() {
        super();
    }

    public Machine_on_off(String machineId, LocalTime startTime, LocalTime endTime) {
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

    public LocalTime getStartTime() {
        return startTime;
    }

    public void setStartTime(LocalTime startTime) {
        this.startTime = startTime;
    }

    public LocalTime getEndTime() {
        return endTime;
    }

    public void setEndTime(LocalTime endTime) {
        this.endTime = endTime;
    }
}
