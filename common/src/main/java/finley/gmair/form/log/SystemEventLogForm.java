package finley.gmair.form.log;

/**
 * @author shenghaohu@hshvip.com
 * @date 2018/6/5
 */
public class SystemEventLogForm {
    private String eventStatus;

    private String module;

    private String logDetail;

    private String ip;

    public String getEventStatus() {
        return eventStatus;
    }

    public void setEventStatus(String eventStatus) {
        this.eventStatus = eventStatus;
    }

    public String getModule() {
        return module;
    }

    public void setModule(String module) {
        this.module = module;
    }

    public String getLogDetail() {
        return logDetail;
    }

    public void setLogDetail(String logDetail) {
        this.logDetail = logDetail;
    }

    public String getIp() {
        return ip;
    }

    public void setIp(String ip) {
        this.ip = ip;
    }
}