package finley.gmair.model.membership;

import finley.gmair.model.Entity;

import java.sql.Timestamp;

/**
 * @ClassName MembershipConsumer
 * @Description 会员表
 * @Author Joby
 * @Date 2021/7/16 16:37
 */
public class MembershipConsumer extends Entity {
    private String consumerId;
    private Integer firstIntegral;
    private Integer secondIntegral;
    public MembershipConsumer(String consumerId){
        this.consumerId = consumerId;
        this.firstIntegral = 0;
        this.secondIntegral = 0;
    }

    public MembershipConsumer(String consumerId, Integer firstIntegral, Integer secondIntegral, Timestamp createAt) {
        this.consumerId = consumerId;
        this.firstIntegral = firstIntegral;
        this.secondIntegral = secondIntegral;
        this.createAt = createAt;
    }

    public String getConsumerId() {
        return consumerId;
    }

    public void setConsumerId(String consumerId) {
        this.consumerId = consumerId;
    }

    public Integer getFirstIntegral() {
        return firstIntegral;
    }

    public void setFirstIntegral(Integer firstIntegral) {
        this.firstIntegral = firstIntegral;
    }

    public Integer getSecondIntegral() {
        return secondIntegral;
    }

    public void setSecondIntegral(Integer secondIntegral) {
        this.secondIntegral = secondIntegral;
    }
}
