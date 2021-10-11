package finley.gmair.model.mqtt;

import finley.gmair.model.Entity;


public class LoggerRecord extends Entity {

    private String recordId;

    private String topicContext;

    private String payloadContext;

    private String machineId;

    public String getMachineId() {
        return machineId;
    }

    public void setMachineId(String machineId) {
        this.machineId = machineId;
    }

    public String getRecordId() {
        return recordId;
    }

    public void setRecordId(String recordId) {
        this.recordId = recordId;
    }

    public String getTopicContext() {
        return topicContext;
    }

    public void setTopicContext(String topicContext) {
        this.topicContext = topicContext;
    }

    public String getPayloadContext() {
        return payloadContext;
    }

    public void setPayloadContext(String payloadContext) {
        this.payloadContext = payloadContext;
    }

}
