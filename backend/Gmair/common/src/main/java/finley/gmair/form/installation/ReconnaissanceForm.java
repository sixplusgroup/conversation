package finley.gmair.form.installation;

public class ReconnaissanceForm {

    private String orderId;

    private String setupMethod;

    private String description;

    private String reconDate;

    private int reconStatus;

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

    public String getReconDate() {
        return reconDate;
    }

    public void setReconDate(String reconDate) {
        this.reconDate = reconDate;
    }

    public int getReconStatus() {
        return reconStatus;
    }

    public void setReconStatus(int reconStatus) {
        this.reconStatus = reconStatus;
    }
}
