package finley.gmair.model.minivoice;

import finley.gmair.model.Entity;

/**
 * 对用户操作进行埋点时的存储结构
 */
public class OperateData extends Entity {

    String purePhoneNumber;

    int platformType;

    int operateType;

    long timeSpent;

    public OperateData() {
        super();
    }

    public OperateData(String purePhoneNumber, int platformType, int operateType, long timeSpent) {
        this.purePhoneNumber = purePhoneNumber;
        this.platformType = platformType;
        this.operateType = operateType;
        this.timeSpent = timeSpent;
    }

    public long getTimeSpent() {
        return timeSpent;
    }

    public void setTimeSpent(long timeSpent) {
        this.timeSpent = timeSpent;
    }

    public String getPurePhoneNumber() {
        return purePhoneNumber;
    }

    public void setPurePhoneNumber(String purePhoneNumber) {
        this.purePhoneNumber = purePhoneNumber;
    }

    public int getPlatformType() {
        return platformType;
    }

    public void setPlatformType(int platformType) {
        this.platformType = platformType;
    }

    public int getOperateType() {
        return operateType;
    }

    public void setOperateType(int operateType) {
        this.operateType = operateType;
    }

    @Override
    public String toString() {
        return "OperateData{" +
                "purePhoneNumber='" + purePhoneNumber + '\'' +
                ", platformType='" + platformType + '\'' +
                ", operateType='" + operateType + '\'' +
                ", timeSpent=" + timeSpent +
                '}';
    }

}
