package finley.gmair.model.express;

import finley.gmair.model.Entity;

import java.sql.Timestamp;

public class ExpressOrder extends Entity {

    private String expressId;

    private String orderId;

    private String companyId;

    private String expressNo;

    private ExpressStatus expressStatus;

    private Timestamp deliverTime;

    private Timestamp receiveTime;

    public ExpressOrder() {
        super();
    }

    public ExpressOrder(String orderId, String companyId, String expressNo) {
        this();
        this.orderId = orderId;
        this.companyId = companyId;
        this.expressNo = expressNo;
        this.expressStatus = ExpressStatus.ASSIGNED;
    }

    public String getExpressId() { return expressId; }

    public void setExpressId(String expressId) { this.expressId = expressId; }

    public String getOrderId() { return orderId; }

    public void setOrderId(String orderId) { this.orderId = orderId; }

    public String getCompanyId() { return companyId; }

    public void setCompanyId(String companyId) { this.companyId = companyId; }

    public String getExpressNo() { return expressNo; }

    public void setExpressNo(String expressNo) { this.expressNo = expressNo; }

    public ExpressStatus getExpressStatus() {
        return expressStatus;
    }

    public void setExpressStatus(ExpressStatus expressStatus) {
        this.expressStatus = expressStatus;
    }

    public Timestamp getDeliverTime() { return deliverTime; }

    public void setDeliverTime(Timestamp deliverTime) { this.deliverTime = deliverTime; }

    public Timestamp getReceiveTime() { return receiveTime; }

    public void setReceiveTime(Timestamp receiveTime) { this.receiveTime = receiveTime; }
}
