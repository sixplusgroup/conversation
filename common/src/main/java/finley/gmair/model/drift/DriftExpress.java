package finley.gmair.model.drift;

import finley.gmair.model.Entity;
import finley.gmair.model.drift.DriftExpressStatus;

public class DriftExpress extends Entity {

    private String expressId;

    private String orderId;

    private String company;

    private String expressNum;

    private DriftExpressStatus status;

    public DriftExpress(){
        super();
        this.status = DriftExpressStatus.DELIVERED;
    }

    public DriftExpress(String expressId, String orderId, String company, String expressNum){
        this();
        this.expressId=expressId;
        this.orderId=orderId;
        this.company=company;
        this.expressNum = expressNum;
    }

    public String getOrderId() {
        return orderId;
    }

    public void setOrderId(String orderId) {
        this.orderId = orderId;
    }

    public String getExpressId() {
        return expressId;
    }

    public void setExpressId(String expressId) {
        this.expressId = expressId;
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

    public DriftExpressStatus getStatus() {
        return status;
    }

    public void setStatus(DriftExpressStatus status) {
        this.status = status;
    }
}
