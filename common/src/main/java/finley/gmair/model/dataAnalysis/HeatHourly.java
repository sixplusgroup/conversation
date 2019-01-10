package finley.gmair.model.dataAnalysis;

import finley.gmair.model.Entity;

import java.sql.Timestamp;

public class HeatHourly extends Entity {
    private String statusId;
    private String machineId;
    private int heatOnMinute;
    private int heatOffMinute;

    public HeatHourly() {
        super();
    }

    public HeatHourly(String machineId, int heatOnMinute, int heatOffMinute) {
        super();
        this.machineId = machineId;
        this.heatOnMinute = heatOnMinute;
        this.heatOffMinute = heatOffMinute;
    }

    public HeatHourly(String machineId, int heatOnMinute, int heatOffMinute, Timestamp timestamp) {
        super();
        this.machineId = machineId;
        this.heatOnMinute = heatOnMinute;
        this.heatOffMinute = heatOffMinute;
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

    public int getHeatOnMinute() {
        return heatOnMinute;
    }

    public void setHeatOnMinute(int heatOnMinute) {
        this.heatOnMinute = heatOnMinute;
    }

    public int getHeatOffMinute() {
        return heatOffMinute;
    }

    public void setHeatOffMinute(int heatOffMinute) {
        this.heatOffMinute = heatOffMinute;
    }
}
