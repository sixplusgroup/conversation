package finley.gmair.model.installation;

import finley.gmair.model.Entity;

public class AssignCode extends Entity {
    private String assignId;
    private String codeSerial;

    public AssignCode() {
        super();
    }

    public AssignCode(String assignId, String codeSerial) {
        super();
        this.assignId = assignId;
        this.codeSerial = codeSerial;
    }

    public String getAssignId() {
        return assignId;
    }

    public void setAssignId(String assignId) {
        this.assignId = assignId;
    }

    public String getCodeSerial() {
        return codeSerial;
    }

    public void setCodeSerial(String codeSerial) {
        this.codeSerial = codeSerial;
    }
}
