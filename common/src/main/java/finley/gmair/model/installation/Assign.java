package finley.gmair.model.installation;

import finley.gmair.model.Entity;

import java.sql.Timestamp;

public class Assign extends Entity {
    private String assignId;
    private String qrcode;
    private String teamId;
    private String memberId;
    private Timestamp assignDate;
    public Assign()
    {
        super();
    }

    public Assign(String qrcode, String teamId, String memberId, Timestamp assignDate) {
        this();
        this.qrcode = qrcode;
        this.teamId = teamId;
        this.memberId = memberId;
        this.assignDate = assignDate;
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

    public Timestamp getAssignDate() {
        return assignDate;
    }

    public void setAssignDate(Timestamp assignDate) {
        this.assignDate = assignDate;
    }
}

