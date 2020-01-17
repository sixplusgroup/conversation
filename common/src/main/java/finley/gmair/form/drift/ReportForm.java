package finley.gmair.form.drift;

public class ReportForm {

    private String orderId;

    private String detectDate;

    private String liveDate;

    private String decorateDate;

    private String isClosed;

    private String temperature;

    private String reportTemplateId;

    private String dataItem;

    public String getOrderId() {
        return orderId;
    }

    public void setOrderId(String orderId) {
        this.orderId = orderId;
    }

    public String getDetectDate() {
        return detectDate;
    }

    public void setDetectDate(String detectDate) {
        this.detectDate = detectDate;
    }

    public String getLiveDate() {
        return liveDate;
    }

    public void setLiveDate(String liveDate) {
        this.liveDate = liveDate;
    }

    public String getDecorateDate() {
        return decorateDate;
    }

    public void setDecorateDate(String decorateDate) {
        this.decorateDate = decorateDate;
    }

    public String getIsClosed() {
        return isClosed;
    }

    public void setIsClosed(String isClosed) {
        this.isClosed = isClosed;
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

    public String getDataItem() {
        return dataItem;
    }

    public void setDataItem(String dataItem) {
        this.dataItem = dataItem;
    }
}
