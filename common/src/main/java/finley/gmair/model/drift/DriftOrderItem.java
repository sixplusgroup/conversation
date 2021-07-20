package finley.gmair.model.drift;

import finley.gmair.model.Entity;

public class DriftOrderItem extends Entity {
    private String itemId;

    private String orderId;

    private String itemName;

    private int singleNum;

    private int quantity;

    private int exQuantity;

    private double itemPrice;

    private double totalPrice;

    private double realPrice;

    private String text;

    private String url;

    public DriftOrderItem() {
        super();
    }

    public DriftOrderItem(String itemName, int quantity, double itemPrice) {
        this();
        this.itemName = itemName;
        this.quantity = quantity;
        this.itemPrice = itemPrice;
    }

    public DriftOrderItem(String orderId, String itemName, int quantity, double itemPrice) {
        this(itemName, quantity, itemPrice);
        this.orderId = orderId;
    }

    public String getItemId() {
        return itemId;
    }

    public void setItemId(String itemId) {
        this.itemId = itemId;
    }

    public String getOrderId() {
        return orderId;
    }

    public void setOrderId(String orderId) {
        this.orderId = orderId;
    }

    public String getItemName() {
        return itemName;
    }

    public void setItemName(String itemName) {
        this.itemName = itemName;
    }

    public int getSingleNum() {
        return singleNum;
    }

    public void setSingleNum(int singleNum) {
        this.singleNum = singleNum;
    }

    public int getQuantity() {
        return quantity;
    }

    public void setQuantity(int quantity) {
        this.quantity = quantity;
    }

    public double getItemPrice() {
        return itemPrice;
    }

    public void setItemPrice(double itemPrice) {
        this.itemPrice = itemPrice;
    }

    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public double getTotalPrice() {
        return totalPrice;
    }

    public void setTotalPrice(double totalPrice) {
        this.totalPrice = totalPrice;
    }

    public double getRealPrice() {
        return realPrice;
    }

    public void setRealPrice(double realPrice) {
        this.realPrice = realPrice;
    }

    public int getExQuantity() {
        return exQuantity;
    }

    public void setExQuantity(int exQuantity) {
        this.exQuantity = exQuantity;
    }
}
