package finley.gmair.model.machine;

import finley.gmair.model.Entity;

import java.sql.Timestamp;

public class FilterLight extends Entity {
    private String machineId;
    private boolean lightStatus;

    public FilterLight(){
        super();
    }

    public FilterLight(String machineId, boolean lightStatus) {
        super();
        this.machineId = machineId;
        this.lightStatus = lightStatus;
    }

    public String getMachineId() {
        return machineId;
    }

    public void setMachineId(String machineId) {
        this.machineId = machineId;
    }

    public boolean isLightStatus() {
        return lightStatus;
    }

    public void setLightStatus(boolean lightStatus) {
        this.lightStatus = lightStatus;
    }
}
