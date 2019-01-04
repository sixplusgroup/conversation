package finley.gmair.model.dataAnalysis;

import finley.gmair.model.Entity;

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
}
