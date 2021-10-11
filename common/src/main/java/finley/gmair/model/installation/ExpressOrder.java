package finley.gmair.model.installation;

import java.sql.Timestamp;

public class ExpressOrder {
    private String expressId;

    private String orderId;

    private String company;

    private String expressNum;

    private ExpressOrderStatus status;

    private Timestamp createTime;

    public ExpressOrder(){
        super();
    }

    public ExpressOrder(String expressId, String orderId, String company, String expressNum, ExpressOrderStatus status, Timestamp createTime) {
        this.expressId = expressId;
        this.orderId = orderId;
        this.company = company;
        this.expressNum = expressNum;
        this.status = status;
        this.createTime = createTime;
    }

    public String getExpressId() {
        return expressId;
    }

    public void setExpressId(String expressId) {
        this.expressId = expressId;
    }

    public String getOrderId() {
        return orderId;
    }

    public void setOrderId(String orderId) {
        this.orderId = orderId;
    }

    public String getCompany() {
        return company;
    }

    public void setCompany(String company) {
        this.company = company;
    }

    public String getExpressNum() {
        return expressNum;
    }

    public void setExpressNum(String expressNum) {
        this.expressNum = expressNum;
    }

    public ExpressOrderStatus getStatus() {
        return status;
    }

    public void setStatus(ExpressOrderStatus status) {
        this.status = status;
    }

    public Timestamp getCreateTime() {
        return createTime;
    }

    public void setCreateTime(Timestamp createTime) {
        this.createTime = createTime;
    }
}
