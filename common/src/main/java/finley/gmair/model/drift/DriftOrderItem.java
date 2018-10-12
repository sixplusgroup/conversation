package finley.gmair.model.drift;

import finley.gmair.model.Entity;

public class DriftOrderItem extends Entity {
    private String itemId;

    private String orderId;

    private String itemName;

    private double quantity;

    private double itemPrice;

    private String testTarget;

    public DriftOrderItem() {
        super();
    }

    public DriftOrderItem(String orderId, String itemName, double quantity, double itemPrice, String testTarget) {
        this();
        this.orderId = orderId;
        this.itemName = itemName;
        this.quantity = quantity;
        this.itemPrice = itemPrice;
        this.testTarget = testTarget;
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

    public double getQuantity() {
        return quantity;
    }

    public void setQuantity(double quantity) {
        this.quantity = quantity;
    }

    public double getItemPrice() {
        return itemPrice;
    }

    public void setItemPrice(double itemPrice) {
        this.itemPrice = itemPrice;
    }

    public String getTestTarget() {
        return testTarget;
    }

    public void setTestTarget(String testTarget) {
        this.testTarget = testTarget;
    }
}
