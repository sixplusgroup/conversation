package finley.gmair.model.dataAnalysis;

import finley.gmair.model.Entity;

import java.sql.Timestamp;

public class IndoorPm25Hourly extends Entity {
    private String statusId;
    private String machineId;
    private double averagePm25;
    private int maxPm25;
    private int minPm25;

    public IndoorPm25Hourly() {
        super();
    }

    public IndoorPm25Hourly(String machineId, double averagePm25, int maxPm25, int minPm25) {
        super();
        this.machineId = machineId;
        this.averagePm25 = averagePm25;
        this.maxPm25 = maxPm25;
        this.minPm25 = minPm25;
    }

    public IndoorPm25Hourly(String machineId, double averagePm25, int maxPm25, int minPm25, Timestamp createAt) {
        super();
        this.machineId = machineId;
        this.averagePm25 = averagePm25;
        this.maxPm25 = maxPm25;
        this.minPm25 = minPm25;
        this.createAt = createAt;
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

    public double getAveragePm25() {
        return averagePm25;
    }

    public void setAveragePm25(double averagePm25) {
        this.averagePm25 = averagePm25;
    }

    public int getMaxPm25() {
        return maxPm25;
    }

    public void setMaxPm25(int maxPm25) {
        this.maxPm25 = maxPm25;
    }

    public int getMinPm25() {
        return minPm25;
    }

    public void setMinPm25(int minPm25) {
        this.minPm25 = minPm25;
    }
}
