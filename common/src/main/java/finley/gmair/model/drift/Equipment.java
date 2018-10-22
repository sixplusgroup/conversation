package finley.gmair.model.drift;

import finley.gmair.model.Entity;

public class Equipment extends Entity {
    private String equipId;

    private String equipName;

    public Equipment() {
        super();
    }

    public Equipment(String equipName) {
        this();
        this.equipName = equipName;
    }

    public String getEquipId() {
        return equipId;
    }

    public void setEquipId(String equipId) {
        this.equipId = equipId;
    }

    public String getEquipName() {
        return equipName;
    }

    public void setEquipName(String equipName) {
        this.equipName = equipName;
    }
}
