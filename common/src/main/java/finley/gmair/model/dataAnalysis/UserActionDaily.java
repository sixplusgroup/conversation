package finley.gmair.model.dataAnalysis;

import finley.gmair.model.Entity;

public class UserActionDaily extends Entity {

    private String recordId;
    private String userId;
    private String machineId;
    private String component;
    private int componentTimes;


    public UserActionDaily() {
        super();
    }

    public UserActionDaily(String userId, String machineId, String component, int componentTimes){
        this();
        this.userId = userId;
        this.machineId = machineId;
        this.component = component;
        this.componentTimes = componentTimes;
    }

    public String getRecordId() {
        return recordId;
    }

    public void setRecordId(String recordId) {
        this.recordId = recordId;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public String getMachineId() {
        return machineId;
    }

    public void setMachineId(String machineId) {
        this.machineId = machineId;
    }

    public String getComponent() {
        return component;
    }

    public void setComponent(String component) {
        this.component = component;
    }

    public int getComponentTimes() {
        return componentTimes;
    }

    public void setComponentTimes(int componentTimes) {
        this.componentTimes = componentTimes;
    }
}
