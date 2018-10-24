package finley.gmair.model.machine;

public class FilterLimitConfig {
    private int overCountLimit;
    private int overPm25Limit;

    public FilterLimitConfig(int overCountLimit,int overPm25Limit) {
        this.overCountLimit = overCountLimit;
        this.overPm25Limit = overPm25Limit;
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
