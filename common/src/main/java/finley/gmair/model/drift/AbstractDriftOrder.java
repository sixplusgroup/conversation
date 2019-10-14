package finley.gmair.model.drift;

import com.fasterxml.jackson.annotation.JsonFormat;
import finley.gmair.model.Entity;

import java.util.Date;
import java.util.List;

public abstract class AbstractDriftOrder extends Entity {
    private String orderId;

    private String consumerId;

    private String consignee;

    private String phone;

    private String address;

    private double totalPrice;

    private Date expectedDate;

    private Date createTime;

    private int intervalDate;

    public AbstractDriftOrder() {
        super();
    }

    public AbstractDriftOrder(String consumerId, String consignee, String phone, String address, Date expectedDate, int intervalDate) {
        this();
        this.createTime = super.createAt;
        this.consumerId = consumerId;
        this.consignee = consignee;
        this.phone = phone;
        this.address = address;
        this.expectedDate = expectedDate;
        this.intervalDate = intervalDate;
    }

    public Date getCreateTime() {
        return createTime;
    }

    public void setCreateTime(Date createTime) {
        this.createTime = createTime;
    }

    public String getOrderId() {
        return orderId;
    }

    public void setOrderId(String orderId) {
        this.orderId = orderId;
    }

    public String getConsumerId() {
        return consumerId;
    }

    public void setConsumerId(String consumerId) {
        this.consumerId = consumerId;
    }

    public String getConsignee() {
        return consignee;
    }

    public void setConsignee(String consignee) {
        this.consignee = consignee;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public double getTotalPrice() {
        return totalPrice;
    }

    public void setTotalPrice(double totalPrice) {
        this.totalPrice = totalPrice;
    }

    public Date getExpectedDate() {
        return expectedDate;
    }

    public void setExpectedDate(Date expectedDate) {
        this.expectedDate = expectedDate;
    }

    public int getIntervalDate() {
        return intervalDate;
    }

    public void setIntervalDate(int intervalDate) {
        this.intervalDate = intervalDate;
    }
}
