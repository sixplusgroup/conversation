package finley.gmair.model.log;

/**
 * @author shenghaohu@hshvip.com
 * @date 2018/6/5
 */
public class SystemEventLog extends AbstractLog{
    private String eventId;

    private String module;

    private String eventStatus;

    private long time;

    public SystemEventLog(String module, String eventStatus, long time, String logDetail, String ip) {
        super(logDetail, ip);
        this.module = module;
        this.eventStatus = eventStatus;
        this.time = time;
    }

    public String getEventId() {
        return eventId;
    }

    public void setEventId(String eventId) {
        this.eventId = eventId;
    }

    public String getModule() {
        return module;
    }

    public void setModule(String module) {
        this.module = module;
    }

    public String getEventStatus() {
        return eventStatus;
    }

    public void setEventStatus(String eventStatus) {
        this.eventStatus = eventStatus;
    }

    public long getTime() {
        return time;
    }

    public void setTime(long time) {
        this.time = time;
    }
}