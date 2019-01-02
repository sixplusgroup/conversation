package finley.gmair.model.machine;

import finley.gmair.model.Entity;
import finley.gmair.util.IDGenerator;


public class MachinePowerDaily extends Entity {
    private String statusId;
    private String machineId;
    private int powerUsage;
    public MachinePowerDaily(){
        super();
    }
    public MachinePowerDaily(String machineId,int powerUsage){
        super();
        this.machineId = machineId;
        this.powerUsage = powerUsage;
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

    public int getPowerUsage() {
        return powerUsage;
    }

    public void setPowerUsage(int powerUsage) {
        this.powerUsage = powerUsage;
    }
}
