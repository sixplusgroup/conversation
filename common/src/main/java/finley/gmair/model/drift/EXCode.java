package finley.gmair.model.drift;

import finley.gmair.model.Entity;

public class EXCode extends Entity {
    private String codeId;

    private String activityId;

    private String codeValue;

    private String label;

    private EXCodeStatus status;

    private double price;

    public EXCode() {
        super();
        this.status = EXCodeStatus.CREATED;
        this.label = "果麦检测";
    }

    public EXCode(String activityId, String codeValue, double price) {
        this();
        this.activityId = activityId;
        this.codeValue = codeValue;
        this.price = price;
    }

    public EXCode(String activityId, String codeValue, double price,String label) {
        this();
        this.activityId = activityId;
        this.codeValue = codeValue;
        this.price = price;
        this.label = label;
    }

    public String getCodeId() {
        return codeId;
    }

    public void setCodeId(String codeId) {
        this.codeId = codeId;
    }

    public String getActivityId() {
        return activityId;
    }

    public void setActivityId(String activityId) {
        this.activityId = activityId;
    }

    public String getCodeValue() {
        return codeValue;
    }

    public void setCodeValue(String codeValue) {
        this.codeValue = codeValue;
    }

    public EXCodeStatus getStatus() {
        return status;
    }

    public void setStatus(EXCodeStatus status) {
        this.status = status;
    }

    public double getPrice() {
        return price;
    }

    public void setPrice(double price) {
        this.price = price;
    }

    public String getLabel() {
        return label;
    }

    public void setLabel(String label) {
        this.label = label;
    }
}
