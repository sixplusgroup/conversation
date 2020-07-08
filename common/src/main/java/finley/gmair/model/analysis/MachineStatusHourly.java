package finley.gmair.model.analysis;

import finley.gmair.model.Entity;

/**
 * @ClassName: MachineStatusHourly
 * @Description: TODO
 * @Author fan
 * @Date 2020/5/19 3:31 PM
 */
public class MachineStatusHourly extends Entity {
    private String machineId;

    //设备一小时的出风口PM2.5数值
    private double averagePm25;
    private int minPm25;
    private int maxPm25;

    //设备一小时的滤网处PM2.5数值
    private double averagePm25Inner;
    private int minPm25Inner;
    private int maxPm25Inner;

    //设备一小时的运行风量
    private double averageVolume;
    private int minSpeed;
    private int maxSpeed;

    //设备一小时的温度
    private double averageTemp;
    private int minTemp;
    private int maxTemp;

    //设置一小时的湿度
    private double averageHumid;
    private int maxHumid;
    private int minHumid;

    //设备一小时的二氧化碳
    private double averageCo2;
    private int maxCo2;
    private int minCo2;

    //设备一小时的运行时长
    private int powerOnMinute;
    private int powerOffMinute;

    //设备一小时的运行模式
    private int autoMinute;
    private int manualMinute;
    private int sleepMinute;

    //设备一小时的辅热时长
    private int heatOnMinute;
    private int heatOffMinute;

    public MachineStatusHourly() {
        super();
    }

    public MachineStatusHourly(String machineId) {
        super();
        this.machineId = machineId;
    }

    public void setPM2_5Indoor(double avg, int min, int max) {
        this.averagePm25 = avg;
        this.minPm25 = min;
        this.maxPm25 = max;
    }

    public void setPM2_5Inner(double avg, int min, int max) {
        this.averagePm25Inner = avg;
        this.minPm25Inner = min;
        this.maxPm25Inner = max;
    }

    public void setSpeed(double avg, int min, int max) {
        this.averageVolume = avg;
        this.minSpeed = min;
        this.maxSpeed = max;
    }

    public void setTemp(double avg, int min, int max) {
        this.averageTemp = avg;
        this.minTemp = min;
        this.maxTemp = max;
    }

    public void setHumid(double avg, int min, int max) {
        this.averageHumid = avg;
        this.minHumid = min;
        this.maxHumid = max;
    }

    public void setCo2(double avg, int min, int max) {
        this.averageCo2 = avg;
        this.minCo2 = min;
        this.maxCo2 = max;
    }

    public void setPower(int onMinute, int offMinute) {
        this.powerOnMinute = onMinute;
        this.powerOffMinute = offMinute;
    }

    public void setMode(int auto, int manual, int sleep) {
        this.autoMinute = auto;
        this.manualMinute = manual;
        this.sleepMinute = sleep;
    }

    public void setHeat(int onMinute, int offMinute) {
        this.heatOnMinute = onMinute;
        this.heatOffMinute = offMinute;
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

    public int getMinPm25() {
        return minPm25;
    }

    public void setMinPm25(int minPm25) {
        this.minPm25 = minPm25;
    }

    public int getMaxPm25() {
        return maxPm25;
    }

    public void setMaxPm25(int maxPm25) {
        this.maxPm25 = maxPm25;
    }

    public double getAveragePm25Inner() {
        return averagePm25Inner;
    }

    public void setAveragePm25Inner(double averagePm25Inner) {
        this.averagePm25Inner = averagePm25Inner;
    }

    public int getMinPm25Inner() {
        return minPm25Inner;
    }

    public void setMinPm25Inner(int minPm25Inner) {
        this.minPm25Inner = minPm25Inner;
    }

    public int getMaxPm25Inner() {
        return maxPm25Inner;
    }

    public void setMaxPm25Inner(int maxPm25Inner) {
        this.maxPm25Inner = maxPm25Inner;
    }

    public double getAverageVolume() {
        return averageVolume;
    }

    public void setAverageVolume(double averageVolume) {
        this.averageVolume = averageVolume;
    }

    public int getMinSpeed() {
        return minSpeed;
    }

    public void setMinSpeed(int minSpeed) {
        this.minSpeed = minSpeed;
    }

    public int getMaxSpeed() {
        return maxSpeed;
    }

    public void setMaxSpeed(int maxSpeed) {
        this.maxSpeed = maxSpeed;
    }

    public double getAverageTemp() {
        return averageTemp;
    }

    public void setAverageTemp(double averageTemp) {
        this.averageTemp = averageTemp;
    }

    public int getMinTemp() {
        return minTemp;
    }

    public void setMinTemp(int minTemp) {
        this.minTemp = minTemp;
    }

    public int getMaxTemp() {
        return maxTemp;
    }

    public void setMaxTemp(int maxTemp) {
        this.maxTemp = maxTemp;
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
