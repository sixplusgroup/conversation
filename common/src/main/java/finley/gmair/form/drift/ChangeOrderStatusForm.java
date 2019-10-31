package finley.gmair.form.drift;

public class ChangeOrderStatusForm {

    private String orderId;

    private String machineOrderNo;

    private String expressNum;

    private String company;

    private String description;

    public void setCompany(String company) {
        this.company = company;
    }

    public String getCompany() {
        return company;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getDescription() {
        return description;
    }

    public String getExpressNum() {
        return expressNum;
    }

    public void setExpressNum(String expressNum) {
        this.expressNum = expressNum;
    }

    public String getMachineOrderNo() {
        return machineOrderNo;
    }

    public void setMachineOrderNo(String machineOrderNo) {
        this.machineOrderNo = machineOrderNo;
    }

    public String getOrderId() {
        return orderId;
    }

    public void setOrderId(String orderId) {
        this.orderId = orderId;
    }
}
