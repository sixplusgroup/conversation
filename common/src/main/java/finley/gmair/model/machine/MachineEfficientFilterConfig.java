package finley.gmair.model.machine;

import finley.gmair.model.Entity;

/**
 * @author: Bright Chan
 * @date: 2020/11/25 20:46
 * @description: MachineEfficientFilterConfig
 */

public class MachineEfficientFilterConfig extends Entity {

    private String configId;

    private boolean tRun;

    private int totalTime;

    private double paramOne;

    private double paramTwo;

    private double firstRemindThreshold;

    private double secondRemindThreshold;

    public MachineEfficientFilterConfig() {
        super();
    }

    public MachineEfficientFilterConfig(String configId, boolean tRun, int totalTime,
                                        double paramOne, double paramTwo,
                                        double firstRemindThreshold, double secondRemindThreshold) {
        super();
        this.configId = configId;
        this.tRun = tRun;
        this.totalTime = totalTime;
        this.paramOne = paramOne;
        this.paramTwo = paramTwo;
        this.firstRemindThreshold = firstRemindThreshold;
        this.secondRemindThreshold = secondRemindThreshold;
    }

    public String getConfigId() {
        return configId;
    }

    public void setConfigId(String configId) {
        this.configId = configId;
    }

    public boolean istRun() {
        return tRun;
    }

    public void settRun(boolean tRun) {
        this.tRun = tRun;
    }

    public int getTotalTime() {
        return totalTime;
    }

    public void setTotalTime(int totalTime) {
        this.totalTime = totalTime;
    }

    public double getParamOne() {
        return paramOne;
    }

    public void setParamOne(double paramOne) {
        this.paramOne = paramOne;
    }

    public double getParamTwo() {
        return paramTwo;
    }

    public void setParamTwo(double paramTwo) {
        this.paramTwo = paramTwo;
    }

    public double getFirstRemindThreshold() {
        return firstRemindThreshold;
    }

    public void setFirstRemindThreshold(double firstRemindThreshold) {
        this.firstRemindThreshold = firstRemindThreshold;
    }

    public double getSecondRemindThreshold() {
        return secondRemindThreshold;
    }

    public void setSecondRemindThreshold(double secondRemindThreshold) {
        this.secondRemindThreshold = secondRemindThreshold;
    }
}
