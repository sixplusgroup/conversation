package finley.gmair.model.order;

import finley.gmair.model.Entity;

import java.util.List;

public abstract class AbstractOrder extends Entity {
    private String orderId;

    protected List<OrderItem> list;

    private String consignee;

    private String phone;

    private String address;

    private double totalPrice;

    public AbstractOrder() {
        super();
    }

    public AbstractOrder(List<OrderItem> list) {
        this();
        this.list = list;
        for (OrderItem item : list) {
            totalPrice += item.getItemPrice();
        }
    }

    public AbstractOrder(List<OrderItem> list, String consignee, String phone, String address) {
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

    public List<OrderItem> getList() {
        return list;
    }

    public void setList(List<OrderItem> list) {
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
