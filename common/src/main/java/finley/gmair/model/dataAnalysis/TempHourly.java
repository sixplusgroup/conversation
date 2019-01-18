package finley.gmair.model.dataAnalysis;

import finley.gmair.model.Entity;

import java.sql.Timestamp;

public class TempHourly extends Entity {
    private String statusId;
    private String machineId;
    private double averageTemp;
    private int maxTemp;
    private int minTemp;

    public TempHourly() {
        super();
    }

    public TempHourly(String machineId, double averageTemp, int maxTemp, int minTemp) {
        super();
        this.machineId = machineId;
        this.averageTemp = averageTemp;
        this.maxTemp = maxTemp;
        this.minTemp = minTemp;
    }
    public TempHourly(String machineId, double averageTemp, int maxTemp, int minTemp, Timestamp timestamp) {
        super();
        this.machineId = machineId;
        this.averageTemp = averageTemp;
        this.maxTemp = maxTemp;
        this.minTemp = minTemp;
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

    public double getAverageTemp() {
        return averageTemp;
    }

    public void setAverageTemp(double averageTemp) {
        this.averageTemp = averageTemp;
    }

    public int getMaxTemp() {
        return maxTemp;
    }

    public void setMaxTemp(int maxTemp) {
        this.maxTemp = maxTemp;
    }

    public int getMinTemp() {
        return minTemp;
    }

    public void setMinTemp(int minTemp) {
        this.minTemp = minTemp;
    }
}
