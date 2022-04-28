package finley.gmair.model.drift;

import finley.gmair.model.Entity;

import java.util.Date;
import java.util.List;

public class DriftReport extends Entity {

    private String reportId;

    private String consumerId;

    private String orderId;

    private String consumerName;

    private String consumerPhone;

    private String consumerAddress;

    private Date detectDate;

    private Date liveDate;

    private Date decorateDate;

    private Boolean isClosed;

    private String temperature;

    private String reportTemplateId;

    private List<DataItem> list;

    public DriftReport(){
        super();
    }

    public DriftReport(String consumerId,String orderId,String consumerName, String consumerPhone, String consumerAddress, Date detectDate, Boolean isClosed, String reportTemplateId) {
        this();
        this.consumerId = consumerId;
        this.orderId = orderId;
        this.consumerName = consumerName;
        this.consumerPhone = consumerPhone;
        this.consumerAddress = consumerAddress;
        this.detectDate = detectDate;
        this.isClosed = isClosed;
        this.reportTemplateId = reportTemplateId;
    }

    public String getConsumerId() {
        return consumerId;
    }

    public void setConsumerId(String consumerId) {
        this.consumerId = consumerId;
    }

    public String getOrderId() {
        return orderId;
    }

    public void setOrderId(String orderId) {
        this.orderId = orderId;
    }

    public String getReportId() {
        return reportId;
    }

    public void setReportId(String reportId) {
        this.reportId = reportId;
    }

    public String getConsumerName() {
        return consumerName;
    }

    public void setConsumerName(String consumerName) {
        this.consumerName = consumerName;
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

    public Date getDetectDate() {
        return detectDate;
    }

    public void setDetectDate(Date detectDate) {
        this.detectDate = detectDate;
    }

    public Date getLiveDate() {
        return liveDate;
    }

    public void setLiveDate(Date liveDate) {
        this.liveDate = liveDate;
    }

    public Date getDecorateDate() {
        return decorateDate;
    }

    public void setDecorateDate(Date decorateDate) {
        this.decorateDate = decorateDate;
    }

    public Boolean getClosed() {
        return isClosed;
    }

    public void setClosed(Boolean closed) {
        isClosed = closed;
    }

    public String getTemperature() {
        return temperature;
    }

    public void setTemperature(String temperature) {
        this.temperature = temperature;
    }

    public String getReportTemplateId() {
        return reportTemplateId;
    }

    public void setReportTemplateId(String reportTemplateId) {
        this.reportTemplateId = reportTemplateId;
    }

    public List<DataItem> getList() {
        return list;
    }

    public void setList(List<DataItem> list) {
        this.list = list;
    }
}
