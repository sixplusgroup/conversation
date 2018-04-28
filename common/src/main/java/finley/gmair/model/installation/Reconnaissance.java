package finley.gmair.model.installation;

import finley.gmair.model.Entity;

public class Reconnaissance extends Entity {
    private String reconId;

    private String orderId;

    private String setupMethod;

    private String description;

    public Reconnaissance() {
        super();
    }

    public Reconnaissance(String orderId, String setupMethod) {
        this();
        this.orderId = orderId;
        this.setupMethod = setupMethod;
    }

    public String getReconId() {
        return reconId;
    }

    public void setReconId(String reconId) {
        this.reconId = reconId;
    }

    public String getOrderId() {
        return orderId;
    }

    public void setOrderId(String orderId) {
        this.orderId = orderId;
    }

    public String getSetupMethod() {
        return setupMethod;
    }

    public void setSetupMethod(String setupMethod) {
        this.setupMethod = setupMethod;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }
}
