package finley.gmair.model.analysis;

import finley.gmair.model.Entity;

import java.sql.Timestamp;

public class HumidHourly extends Entity {
    private String statusId;
    private String machineId;
    private double averageHumid;
    private int maxHumid;
    private int minHumid;

    public HumidHourly() {
        super();
    }

    public HumidHourly(String machineId, double averageHumid, int maxHumid, int minHumid) {
        super();
        this.machineId = machineId;
        this.averageHumid = averageHumid;
        this.maxHumid = maxHumid;
        this.minHumid = minHumid;
    }
    public HumidHourly(String machineId, double averageHumid, int maxHumid, int minHumid,Timestamp timestamp) {
        super();
        this.machineId = machineId;
        this.averageHumid = averageHumid;
        this.maxHumid = maxHumid;
        this.minHumid = minHumid;
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

    public double getAverageHumid() {
        return averageHumid;
    }

    public void setAverageHumid(double averageHumid) {
        this.averageHumid = averageHumid;
    }

    public int getMaxHumid() {
        return maxHumid;
    }

    public void setMaxHumid(int maxHumid) {
        this.maxHumid = maxHumid;
    }

    public int getMinHumid() {
        return minHumid;
    }

    public void setMinHumid(int minHumid) {
        this.minHumid = minHumid;
    }
}
