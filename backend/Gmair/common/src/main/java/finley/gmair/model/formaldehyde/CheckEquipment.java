package finley.gmair.model.formaldehyde;

import finley.gmair.model.Entity;

public class CheckEquipment extends Entity {
    private String equipmentId;
    private String equipmentName;

    public CheckEquipment() {
        super();
    }

    public CheckEquipment(String equipmentId, String equipmentName) {
        super();
        this.equipmentId = equipmentId;
        this.equipmentName = equipmentName;
    }

    public String getEquipmentId() {
        return equipmentId;
    }

    public void setEquipmentId(String equipmentId) {
        this.equipmentId = equipmentId;
    }

    public String getEquipmentName() {
        return equipmentName;
    }

    public void setEquipmentName(String equipmentName) {
        this.equipmentName = equipmentName;
    }
}
