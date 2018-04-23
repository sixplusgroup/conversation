package finley.gmair.model.location;

import finley.gmair.model.Entity;

import java.sql.Timestamp;

public class LocationRetryCount extends Entity{

    private int retryCount;
    private Timestamp updateTime;

    public int getRetryCount() {
        return retryCount;
    }

    public void setRetryCount(int retryCount) {
        this.retryCount = retryCount;
    }

    public Timestamp getUpdateTime() {
        return updateTime;
    }

    public void setUpdateTime(Timestamp updateTime) {
        this.updateTime = updateTime;
    }
}
