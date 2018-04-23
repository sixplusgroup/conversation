package finley.gmair.model.express;

import finley.gmair.model.Entity;

public class ExpressOrder extends Entity {

    private String expressId;

    private String orderId;

    private String companyId;

    private String expressNo;

    private String expressStatus;

    public ExpressOrder() {
        super();
    }

    public ExpressOrder(String orderId, String companyId, String expressNo) {
        this();
        this.orderId=orderId;
        this.companyId=companyId;
        this.expressNo=expressNo;
    }

    public String getExpressId() { return expressId; }

    public void setExpressId(String expressId) { this.expressId = expressId; }

    public String getOrderId() { return orderId; }

    public void setOrderId(String orderId) { this.orderId = orderId; }

    public String getCompanyId() { return companyId; }

    public void setCompanyId(String companyId) { this.companyId = companyId; }

    public String getExpressNo() { return expressNo; }

    public void setExpressNo(String expressNo) { this.expressNo = expressNo; }

    public String getExpressStatus() { return expressStatus; }

    public void setExpressStatus(String expressStatus) { this.expressStatus = expressStatus; }
}
