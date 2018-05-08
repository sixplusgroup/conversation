package finley.gmair.model.installation;

import finley.gmair.model.Entity;

import java.sql.Timestamp;

public class Assign extends Entity {
    private String assignId;
    private String qrcode;
    private String teamId;
    private String memberId;
    private AssignStatus assignStatus;
    private Timestamp assignDate;
    private String consumerConsignee;
    private String consumerPhone;
    private String consumerAddress;
    public Assign()
    {
        super();
        this.assignStatus = AssignStatus.TODOASSIGN;
    }

    public Assign(String qrcode, String teamId, String memberId, Timestamp assignDate, String consumerConsignee, String consumerPhone, String consumerAddress) {
        this();
        this.qrcode = qrcode;
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

    public String getQrcode() {
        return qrcode;
    }

    public void setQrcode(String qrcode) {
        this.qrcode = qrcode;
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

