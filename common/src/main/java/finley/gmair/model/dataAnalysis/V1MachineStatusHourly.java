package finley.gmair.model.dataAnalysis;

import finley.gmair.model.Entity;

public class V1MachineStatusHourly extends Entity {
    //机器id
    private String machineId;

    //一系列统计值
    private double averagePm25;
    private int maxPm25;
    private int minPm25;

    private double averageVolume;
    private int maxVolume;
    private int minVolume;

    private double averageTemp;
    private int maxTemp;
    private int minTemp;

    private double averageHumid;
    private int maxHumid;
    private int minHumid;

    private int powerOnMinute;
    private int powerOffMinute;

    private int autoMinute;
    private int manualMinute;
    private int sleepMinute;

    private int heatOnMinute;
    private int heatOffMinute;

    public V1MachineStatusHourly() {
        super();
    }

    public V1MachineStatusHourly(String machineId, double averagePm25, int maxPm25, int minPm25, double averageVolume, int maxVolume, int minVolume, double averageTemp, int maxTemp, int minTemp, double averageHumid, int maxHumid, int minHumid, int powerOnMinute, int powerOffMinute, int autoMinute, int manualMinute, int sleepMinute, int heatOnMinute, int heatOffMinute) {
        super();
        this.machineId = machineId;
        this.averagePm25 = averagePm25;
        this.maxPm25 = maxPm25;
        this.minPm25 = minPm25;
        this.averageVolume = averageVolume;
        this.maxVolume = maxVolume;
        this.minVolume = minVolume;
        this.averageTemp = averageTemp;
        this.maxTemp = maxTemp;
        this.minTemp = minTemp;
        this.averageHumid = averageHumid;
        this.maxHumid = maxHumid;
        this.minHumid = minHumid;
        this.powerOnMinute = powerOnMinute;
        this.powerOffMinute = powerOffMinute;
        this.autoMinute = autoMinute;
        this.manualMinute = manualMinute;
        this.sleepMinute = sleepMinute;
        this.heatOnMinute = heatOnMinute;
        this.heatOffMinute = heatOffMinute;
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

    public int getPowerOnMinute() {
        return powerOnMinute;
    }

    public void setPowerOnMinute(int powerOnMinute) {
        this.powerOnMinute = powerOnMinute;
    }

    public int getPowerOffMinute() {
        return powerOffMinute;
    }

    public void setPowerOffMinute(int powerOffMinute) {
        this.powerOffMinute = powerOffMinute;
    }

    public int getAutoMinute() {
        return autoMinute;
    }

    public void setAutoMinute(int autoMinute) {
        this.autoMinute = autoMinute;
    }

    public int getManualMinute() {
        return manualMinute;
    }

    public void setManualMinute(int manualMinute) {
        this.manualMinute = manualMinute;
    }

    public int getSleepMinute() {
        return sleepMinute;
    }

    public void setSleepMinute(int sleepMinute) {
        this.sleepMinute = sleepMinute;
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
