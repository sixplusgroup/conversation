package finley.gmair.model.installation;

import finley.gmair.model.Entity;

import java.sql.Timestamp;
import java.util.Date;

public class Assign extends Entity {
    private String assignId;

    private String codeValue;

    private String teamId;

    private String memberId;

    private String companyId;

    private AssignStatus assignStatus;

    private String consumerConsignee;

    private String consumerPhone;

    private String consumerAddress;

    private Date assignDate;

    private String source;

    private String region;

    private String detail;

    private String description;

    private String type;

    public Assign() {
        super();
        this.assignStatus = AssignStatus.TODOASSIGN;
    }

    public Assign(String consumerConsignee, String consumerPhone, String consumerAddress, String detail, String source) {
        this();
        this.consumerConsignee = consumerConsignee;
        this.consumerPhone = consumerPhone;
        this.consumerAddress = consumerAddress;
        this.detail = detail;
        this.source = source;
    }

    public Assign(String consumerConsignee, String consumerPhone, String consumerAddress, String detail, String source, String description) {
        this(consumerConsignee, consumerPhone, consumerAddress, detail, source);
        this.description = description;

    }

    public Assign(String consumerConsignee, String consumerPhone, String consumerAddress, String detail, String source, String description,String companyId) {
        this(consumerConsignee, consumerPhone, consumerAddress, detail, source,description);
        this.companyId = companyId;

    }

    public Assign(String consumerConsignee, String consumerPhone, String consumerAddress, String detail, String source, String description,String companyId, String type) {
        this(consumerConsignee, consumerPhone, consumerAddress, detail, source,description, companyId);
        this.type = type;

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

    public Date getAssignDate() {
        return assignDate;
    }

    public void setAssignDate(Date assignDate) {
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

    public String getSource() {
        return source;
    }

    public void setSource(String source) {
        this.source = source;
    }

    public String getRegion() {
        return region;
    }

    public void setRegion(String region) {
        this.region = region;
    }

    public String getDetail() {
        return detail;
    }

    public void setDetail(String detail) {
        this.detail = detail;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getCompanyId() {
        return companyId;
    }

    public void setCompanyId(String companyId) {
        this.companyId = companyId;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }
}

