package finley.gmair.model.location;

public class OrderLocationRetryCount extends LocationRetryCount{
    private String orderId;

    public String getOrderId() {
        return orderId;
    }

    public void setOrderId(String orderId) {
        this.orderId = orderId;
    }
}
