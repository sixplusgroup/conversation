package finley.gmair.model.installation;

import finley.gmair.model.Entity;

import java.sql.Timestamp;

public class Assign extends Entity {
    private String assignId;

    private String codeValue;

    private String teamId;

    private String memberId;

    private AssignStatus assignStatus;

    private String consumerConsignee;

    private String consumerPhone;

    private String consumerAddress;

    private Timestamp assignDate;

    public Assign() {
        super();
        this.assignStatus = AssignStatus.TODOASSIGN;
    }

    public Assign(String codeValue, String consumerConsignee, String consumerPhone, String consumerAddress) {
        this();
        this.codeValue = codeValue;
        this.consumerConsignee = consumerConsignee;
        this.consumerPhone = consumerPhone;
        this.consumerAddress = consumerAddress;
    }

    public Assign(String codeValue, String teamId, String memberId) {
        this();
        this.codeValue = codeValue;
        this.teamId = teamId;
        this.memberId = memberId;
    }

    public Assign(String codeValue, String teamId, String memberId, Timestamp assignDate, String consumerConsignee, String consumerPhone, String consumerAddress) {
        this();
        this.codeValue = codeValue;
        this.teamId = teamId;
        this.memberId = memberId;
        this.assignDate = assignDate;
        this.consumerConsignee = consumerConsignee;
        this.consumerPhone = consumerPhone;
        this.consumerAddress = consumerAddress;
    }

    public String getAssignId() {
        return assignId;
    }

    public void setAssignId(String assignId) {
        this.assignId = assignId;
    }

    public String getCodeValue() {
        return codeValue;
    }

    public void setCodeValue(String codeValue) {
        this.codeValue = codeValue;
    }

    public String getTeamId() {
        return teamId;
    }

    public void setTeamId(String teamId) {
        this.teamId = teamId;
    }

    public String getMemberId() {
        return memberId;
    }

    public void setMemberId(String memberId) {
        this.memberId = memberId;
    }

    public AssignStatus getAssignStatus() {
        return assignStatus;
    }

    public void setAssignStatus(AssignStatus assignStatus) {
        this.assignStatus = assignStatus;
    }

    public Timestamp getAssignDate() {
        return assignDate;
    }

    public void setAssignDate(Timestamp assignDate) {
        this.assignDate = assignDate;
    }

    public String getConsumerConsignee() {
        return consumerConsignee;
    }

    public void setConsumerConsignee(String consumerConsignee) {
        this.consumerConsignee = consumerConsignee;
    }

    public String getConsumerPhone() {
        return consumerPhone;
    }

    public void setConsumerPhone(String consumerPhone) {
        this.consumerPhone = consumerPhone;
    }

    public String getConsumerAddress() {
        return consumerAddress;
    }

    public void setConsumerAddress(String consumerAddress) {
        this.consumerAddress = consumerAddress;
    }
}

