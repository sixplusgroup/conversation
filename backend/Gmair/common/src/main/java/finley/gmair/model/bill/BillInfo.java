package finley.gmair.model.bill;

import finley.gmair.model.Entity;

public class BillInfo extends Entity {
    private String billId;

    private String orderId;

    private double orderPrice;

    private double actualPrice;

    private BillStatus status;

    private String accountId;

    private String channelId;

    public BillInfo() {
        super();
        this.status = BillStatus.UNPAYED;
    }

    public BillInfo(String orderId, double actualPrice) {
        this();
        this.orderId = orderId;
        this.actualPrice = actualPrice;
    }

    public String getBillId() {
        return billId;
    }

    public void setBillId(String billId) {
        this.billId = billId;
    }

    public String getOrderId() {
        return orderId;
    }

    public void setOrderId(String orderId) {
        this.orderId = orderId;
    }

    public double getActualPrice() {
        return actualPrice;
    }

    public void setActualPrice(double actualPrice) {
        this.actualPrice = actualPrice;
    }

    public BillStatus getStatus() {
        return status;
    }

    public void setStatus(BillStatus status) {
        this.status = status;
    }

    public double getOrderPrice() { return orderPrice; }

    public void setOrderPrice(double orderPrice) {
        this.orderPrice = orderPrice;
    }

    public String getAccountId() {
        return accountId;
    }

    public void setAccountId(String accountId) {
        this.accountId = accountId;
    }

    public String getChannelId() {
        return channelId;
    }

    public void setChannelId(String channelId) {
        this.channelId = channelId;
    }


}

