package finley.gmair.model.drift;

import finley.gmair.model.Entity;

public class DriftOrderCancel extends Entity {

    private String cancelId;

    private String orderId;

    private String openId;

    private double price;

    private boolean isFinish;

    public DriftOrderCancel(){
        super();
        this.isFinish = false;
    }

    public DriftOrderCancel(String orderId,String openId,double price){
        this();
        this.orderId = orderId;
        this.openId = openId;
        this.price = price;
    }

    public String getCancelId() {
        return cancelId;
    }

    public void setCancelId(String cancelId) {
        this.cancelId = cancelId;
    }

    public String getOrderId() {
        return orderId;
    }

    public void setOrderId(String orderId) {
        this.orderId = orderId;
    }

    public String getOpenId() {
        return openId;
    }

    public void setOpenId(String openId) {
        this.openId = openId;
    }

    public double getPrice() {
        return price;
    }

    public void setPrice(double price) {
        this.price = price;
    }

    public boolean isFinish() {
        return isFinish;
    }

    public void setFinish(boolean finish) {
        isFinish = finish;
    }
}
