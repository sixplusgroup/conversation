package finley.gmair.model.installation;

import finley.gmair.model.Entity;

import java.sql.Timestamp;
import java.util.Date;

/**
 * 用户预约 自创工单
 */
public class Userassign extends Entity {

    private String userassignId;

    private String consumerConsignee;

    private String consumerPhone;

    private String consumerAddress;

    private String userassignDetail;

    private int userassignStatus;// 0 未确认 1 已确认

    private Timestamp userassignDate;

    private String userassignType;

    private String remarks;



    public Userassign() {
        super();
        this.userassignStatus = 0;
    }

    public String getUserassignId() {
        return userassignId;
    }

    public void setUserassignId(String userassignId) {
        this.userassignId = userassignId;
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

    public String getUserassignDetail() {
        return userassignDetail;
    }

    public void setUserassignDetail(String userassignDetail) {
        this.userassignDetail = userassignDetail;
    }

    public int getUserassignStatus() {
        return userassignStatus;
    }

    public void setUserassignStatus(int userassignStatus) {
        this.userassignStatus = userassignStatus;
    }

    public Timestamp getUserassignDate() {
        return userassignDate;
    }

    public void setUserassignDate(Timestamp userassignDate) {
        this.userassignDate = userassignDate;
    }

    public String getUserassignType() {
        return userassignType;
    }

    public void setUserassignType(String userassignType) {
        this.userassignType = userassignType;
    }

    public String getRemarks() {
        return remarks;
    }

    public void setRemarks(String remarks) {
        this.remarks = remarks;
    }
}

