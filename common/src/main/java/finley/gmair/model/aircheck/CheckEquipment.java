package finley.gmair.model.aircheck;

import finley.gmair.model.Entity;

public class CheckEquipment extends Entity {
    private String equipmentId;

    private String equimentName;

    public CheckEquipment() {
        super();
    }

    public CheckEquipment(String equimentName) {
        this();
        this.equimentName = equimentName;
    }

    public String getEquipmentId() {
        return equipmentId;
    }

    public void setEquipmentId(String equipmentId) {
        this.equipmentId = equipmentId;
    }

    public String getEquimentName() {
        return equimentName;
    }

    public void setEquimentName(String equimentName) {
        this.equimentName = equimentName;
    }
}
