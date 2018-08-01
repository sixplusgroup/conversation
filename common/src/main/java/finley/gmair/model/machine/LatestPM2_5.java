package finley.gmair.model.machine;

import finley.gmair.model.Entity;

public class LatestPM2_5 extends Entity {
    private String latestId;
    private String machineId;
    private int pm2_5;
    public LatestPM2_5(){
        super();
    }

    public LatestPM2_5(String machineId, int pm2_5) {
        super();
        this.machineId = machineId;
        this.pm2_5 = pm2_5;
    }

    public String getLatestId() {
        return latestId;
    }

    public void setLatestId(String latestId) {
        this.latestId = latestId;
    }

    public String getMachineId() {
        return machineId;
    }

    public void setMachineId(String machineId) {
        this.machineId = machineId;
    }

    public double getPm2_5() {
        return pm2_5;
    }

    public void setPm2_5(int pm2_5) {
        this.pm2_5 = pm2_5;
    }
}
