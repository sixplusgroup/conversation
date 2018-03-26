package finley.gmair.model.order;

import finley.gmair.model.Entity;

public abstract class AbstractOrderItem extends Entity {
    private String itemId;

    private String itemName;

    private double quantity;

    private double itemPrice;

    public AbstractOrderItem() {
        super();
    }

    public AbstractOrderItem(String itemName, double quantity, double itemPrice) {
        this();
        this.itemName = itemName;
        this.quantity = quantity;
        this.itemPrice = itemPrice;
    }

    public String getItemId() {
        return itemId;
    }

    public void setItemId(String itemId) {
        this.itemId = itemId;
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
}
