package finley.gmair.model.membership;

import finley.gmair.model.Entity;

import java.sql.Timestamp;

/**
 * @ClassName IntegralRecord
 * @Description 积分记录表
 * @Author Joby
 * @Date 2021/7/16 16:37
 */
public class IntegralRecord extends Entity {
    private String recordId;
    private String consumerId;
    private String productId;
    private Integer integralValue;
    private boolean isConfirmed;
    private Timestamp confirmedTime;
    public IntegralRecord(){

    }

    public String getRecordId() {
        return recordId;
    }

    public void setRecordId(String recordId) {
        this.recordId = recordId;
    }

    public String getConsumerId() {
        return consumerId;
    }

    public void setConsumerId(String consumerId) {
        this.consumerId = consumerId;
    }

    public String getProductId() {
        return productId;
    }

    public void setProductId(String productId) {
        this.productId = productId;
    }

    public Integer getIntegralValue() {
        return integralValue;
    }

    public void setIntegralValue(Integer integralValue) {
        this.integralValue = integralValue;
    }

    public boolean isConfirmed() {
        return isConfirmed;
    }

    public void setConfirmed(boolean confirmed) {
        isConfirmed = confirmed;
    }

    public Timestamp getConfirmedTime() {
        return confirmedTime;
    }

    public void setConfirmedTime(Timestamp confirmedTime) {
        this.confirmedTime = confirmedTime;
    }
}
