package finley.gmair.model.drift;

import finley.gmair.model.Entity;

public class EXCode extends Entity {
    private String codeId;

    private String activityId;

    private String codeValue;

    private EXCodeStatus status;

    private CodeUseType type;

    public EXCode() {
        super();
        this.status = EXCodeStatus.CREATED;
    }

    public EXCode(String activityId, String codeValue, EXCodeStatus status) {
        this();
        this.activityId = activityId;
        this.codeValue = codeValue;
        this.status = status;
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

    public CodeUseType getType() {
        return type;
    }

    public void setType(CodeUseType type) {
        this.type = type;
    }
}
