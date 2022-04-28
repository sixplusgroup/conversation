package finley.gmair.model.installation;

import finley.gmair.model.Entity;

import java.sql.Timestamp;

public class Reconnaissance extends Entity {
    private String reconId;

    private String orderId;

    private String setupMethod;

    private String description;

    private ReconnaissanceStatus status;

    private Timestamp reconnaissanceDate;

    public Reconnaissance() {
        super();
        this.status = ReconnaissanceStatus.TODO;
    }

    public Reconnaissance(String orderId, String setupMethod) {
        this();
        this.orderId = orderId;
        this.setupMethod = setupMethod;
        this.status = ReconnaissanceStatus.TODO;
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

    public ReconnaissanceStatus getStatus() {
        return status;
    }

    public void setStatus(ReconnaissanceStatus status) {
        this.status = status;
    }

    public Timestamp getReconnaissanceDate() {
        return reconnaissanceDate;
    }

    public void setReconnaissanceDate(Timestamp reconnaissanceDate) {
        this.reconnaissanceDate = reconnaissanceDate;
    }
}
