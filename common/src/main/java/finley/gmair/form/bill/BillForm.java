package finley.gmair.form.bill;

import finley.gmair.model.bill.BillStatus;

public class BillForm {
    private String billId;

    private String orderId;

    private double orderPrice;

    private double actualPrice;

    private BillStatus status;

    public String getBillId(){return  billId; }

    public void setBillId(String billId) {
        this.billId = billId;
    }

    public String getOrderId(){return orderId; }

    public void setOrderId(String orderId) {
        this.orderId = orderId;
    }

    public double getActualPrice(){return actualPrice; }

    public void setActualPrice(double actualPrice) {
        this.actualPrice = actualPrice;
    }

    public double getOrderPrice(){return orderPrice; }

    public void setOrderPrice(double orderPrice) {
        this.orderPrice = orderPrice;
    }

    public BillStatus getStatus() {
        return status;
    }

    public void setStatus(BillStatus status) {
        this.status = status;
    }
}
