package finley.gmair.model.drift;

import finley.gmair.model.Entity;

public class Express extends Entity {

    private String orderId;

    private String expressId;

    private ExpressStatus status;

    private String company;

    public Express(){}

    public Express(String orderId, String expressId, String company){
        this.orderId=orderId;
        this.expressId=expressId;
        this.company=company;
        //this.status=ExpressStatus.DELIVERED;
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

    public ExpressStatus getStatus() {
        return status;
    }

    public void setStatus(ExpressStatus status) {
        this.status = status;
    }

    public String getCompany() {
        return company;
    }

    public void setCompany(String company) {
        this.company = company;
    }
}
