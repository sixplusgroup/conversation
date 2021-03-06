package finley.gmair.model.machine;

import finley.gmair.model.Entity;

/**
 * Created by sunshine on 10/12/2017.
 */
public class Insight extends Entity {
    private String insightId;

    private String machineId;

    private String eventId;

        private String path;

    public Insight() {
        super();
    }

    public Insight(String machineId, String path) {
        this();
        this.machineId = machineId;
        this.path = path;
    }

    public Insight(String codeId, String eventId, String path) {
        this(codeId, path);
        this.eventId = eventId;
    }

    public String getInsightId() {
        return insightId;
    }

    public void setInsightId(String insightId) {
        this.insightId = insightId;
    }

    public String getMachineId() {
        return machineId;
    }

    public void setMachineId(String machineId) {
        this.machineId = machineId;
    }

    public String getEventId() {
        return eventId;
    }

    public void setEventId(String eventId) {
        this.eventId = eventId;
    }

    public String getPath() {
        return path;
    }

    public void setPath(String path) {
        this.path = path;
    }
}
