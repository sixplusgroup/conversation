package finley.gmair.model.drift;

import finley.gmair.model.Entity;

import java.util.List;

public abstract class AbstractDriftOrder extends Entity {
    private String orderId;

    protected List<DriftOrderItem> list;

    private String consignee;

    private String phone;

    private String address;

    private double totalPrice;

    public AbstractDriftOrder() {
        super();
    }

    public AbstractDriftOrder(List<DriftOrderItem> list) {
        this();
        this.list = list;
        for (DriftOrderItem item : list) {
            totalPrice += item.getItemPrice();
        }
    }

    public AbstractDriftOrder(List<DriftOrderItem> list, String consignee, String phone, String address) {
        this(list);
        this.consignee = consignee;
        this.phone = phone;
        this.address = address;
    }

    public String getOrderId() {
        return orderId;
    }

    public void setOrderId(String orderId) {
        this.orderId = orderId;
    }

    public List<DriftOrderItem> getList() {
        return list;
    }

    public void setList(List<DriftOrderItem> list) {
        this.list = list;
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
}
