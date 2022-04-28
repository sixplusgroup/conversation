package finley.gmair.model.machine;

import finley.gmair.model.Entity;

public class OutPm25Daily extends Entity {
    private String recordId;
    private String machineId;
    private double averagePm25;
    private int overCount;
    public OutPm25Daily(){
        super();
    }

    public OutPm25Daily(String machineId, double averagePm25, int overCount) {
        super();
        this.machineId = machineId;
        this.averagePm25 = averagePm25;
        this.overCount = overCount;
    }


    public String getRecordId() {
        return recordId;
    }

    public void setRecordId(String recordId) {
        this.recordId = recordId;
    }

    public String getMachineId() {
        return machineId;
    }

    public void setMachineId(String machineId) {
        this.machineId = machineId;
    }

    public double getAveragePm25() {
        return averagePm25;
    }

    public void setAveragePm25(double averagePm25) {
        this.averagePm25 = averagePm25;
    }

    public int getOverCount() {
        return overCount;
    }

    public void setOverCount(int overCount) {
        this.overCount = overCount;
    }
}
