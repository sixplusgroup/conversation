package finley.gmair.model.machine;

import finley.gmair.model.Entity;

import java.sql.Timestamp;

public class OutPm25Hourly extends Entity {
    private String latestId;
    private String machineId;
    private int pm2_5;
    private int indexHour;
    public OutPm25Hourly(){
        super();
    }

    public OutPm25Hourly(String machineId, int pm2_5, int indexHour) {
        super();
        this.machineId = machineId;
        this.pm2_5 = pm2_5;
        this.indexHour = indexHour;
    }

    public OutPm25Hourly(String machineId, Timestamp timestamp){
        this.machineId = machineId;
        this.pm2_5 = 0;
        this.indexHour = 0;
        this.blockFlag = false;
        this.createAt = timestamp;
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

    public int getPm2_5() {
        return pm2_5;
    }

    public void setPm2_5(int pm2_5) {
        this.pm2_5 = pm2_5;
    }

    public int getIndexHour() {
        return indexHour;
    }

    public void setIndexHour(int indexHour) {
        this.indexHour = indexHour;
    }
}
