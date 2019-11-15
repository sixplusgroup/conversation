package finley.gmair.model.machine;

import finley.gmair.model.Entity;

public class FilterLimitConfig extends Entity {

    private String configId;

    private String modelId;

    private int overCountLimit;

    private int overPm25Limit;

    private int duration;

    public FilterLimitConfig(int overCountLimit, int overPm25Limit, int duration) {
        this.overCountLimit = overCountLimit;
        this.overPm25Limit = overPm25Limit;
        this.duration = duration;
    }

    public String getConfigId() {
        return configId;
    }

    public void setConfigId(String configId) {
        this.configId = configId;
    }

    public String getModelId() {
        return modelId;
    }

    public void setModelId(String modelId) {
        this.modelId = modelId;
    }

    public int getDuration() {
        return duration;
    }

    public void setDuration(int duration) {
        this.duration = duration;
    }

    public int getOverCountLimit() {
        return overCountLimit;
    }

    public void setOverCountLimit(int overCountLimit) {
        this.overCountLimit = overCountLimit;
    }

    public int getOverPm25Limit() {
        return overPm25Limit;
    }

    public void setOverPm25Limit(int overPm25Limit) {
        this.overPm25Limit = overPm25Limit;
    }
}
