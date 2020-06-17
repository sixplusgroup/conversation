package finley.gmair.model.analysis;

import finley.gmair.model.Entity;

import java.sql.Timestamp;

public class VolumeHourly extends Entity {
    private String statusId;
    private String machineId;
    private double averageVolume;
    private int maxVolume;
    private int minVolume;


    public VolumeHourly() {
        super();
    }

    public VolumeHourly(String machineId, double averageVolume, int maxVolume, int minVolume) {
        super();
        this.machineId = machineId;
        this.averageVolume = averageVolume;
        this.maxVolume = maxVolume;
        this.minVolume = minVolume;
    }

    public VolumeHourly(String machineId, double averageVolume, int maxVolume, int minVolume, Timestamp timestamp) {
        super();
        this.machineId = machineId;
        this.averageVolume = averageVolume;
        this.maxVolume = maxVolume;
        this.minVolume = minVolume;
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

    public double getAverageVolume() {
        return averageVolume;
    }

    public void setAverageVolume(double averageVolume) {
        this.averageVolume = averageVolume;
    }

    public int getMaxVolume() {
        return maxVolume;
    }

    public void setMaxVolume(int maxVolume) {
        this.maxVolume = maxVolume;
    }

    public int getMinVolume() {
        return minVolume;
    }

    public void setMinVolume(int minVolume) {
        this.minVolume = minVolume;
    }
}
