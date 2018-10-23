package finley.gmair.model.drift;

import finley.gmair.model.Entity;

public class EquipActivity extends Entity {
    private String equipId;

    private String activityId;

    public EquipActivity() {
        super();
    }

    public EquipActivity(String equipId, String activityId) {
        this();
        this.equipId = equipId;
        this.activityId = activityId;
    }

    public String getEquipId() {
        return equipId;
    }

    public void setEquipId(String equipId) {
        this.equipId = equipId;
    }

    public String getActivityId() {
        return activityId;
    }

    public void setActivityId(String activityId) {
        this.activityId = activityId;
    }
}
