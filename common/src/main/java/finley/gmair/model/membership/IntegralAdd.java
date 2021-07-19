package finley.gmair.model.membership;

import finley.gmair.model.Entity;

import java.sql.Timestamp;

/**
 * @ClassName IntegralRecord
 * @Description 积分记录表
 * @Author Joby
 * @Date 2021/7/16 16:37
 */
public class IntegralAdd extends Entity {
    private String addId;
    private String consumerId;
    private String productId;
    private Integer integralValue;
    private boolean isConfirmed;
    private Timestamp confirmedTime;

    public IntegralAdd(String consumerId,Integer integralValue, String productId){
        this.consumerId =consumerId;
        this.integralValue =integralValue;
        this.productId = productId;
        this.isConfirmed = false;
    }

    public IntegralAdd(String addId, String consumerId, String productId, Integer integralValue, boolean isConfirmed, Timestamp confirmedTime,Timestamp createAt) {
        this.addId = addId;
        this.consumerId = consumerId;
        this.productId = productId;
        this.integralValue = integralValue;
        this.isConfirmed = isConfirmed;
        this.confirmedTime = confirmedTime;
        this.createAt = createAt;
    }

    public String getAddId() {
        return addId;
    }

    public void setAddId(String addId) {
        this.addId = addId;
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
