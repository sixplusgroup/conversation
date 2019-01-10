package finley.gmair.model.dataAnalysis;

import finley.gmair.model.Entity;

import java.sql.Timestamp;

public class Co2Hourly extends Entity {
    private String statusId;
    private String machineId;
    private double averageCo2;
    private int maxCo2;
    private int minCo2;

    public Co2Hourly() {
        super();
    }

    public Co2Hourly(String machineId, double averageCo2, int maxCo2, int minCo2) {
        super();
        this.machineId = machineId;
        this.averageCo2 = averageCo2;
        this.maxCo2 = maxCo2;
        this.minCo2 = minCo2;
    }
    public Co2Hourly(String machineId, double averageCo2, int maxCo2, int minCo2, Timestamp timestamp) {
        super();
        this.machineId = machineId;
        this.averageCo2 = averageCo2;
        this.maxCo2 = maxCo2;
        this.minCo2 = minCo2;
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

    public double getAverageCo2() {
        return averageCo2;
    }

    public void setAverageCo2(double averageCo2) {
        this.averageCo2 = averageCo2;
    }

    public int getMaxCo2() {
        return maxCo2;
    }

    public void setMaxCo2(int maxCo2) {
        this.maxCo2 = maxCo2;
    }

    public int getMinCo2() {
        return minCo2;
    }

    public void setMinCo2(int minCo2) {
        this.minCo2 = minCo2;
    }
}
