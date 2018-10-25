package finley.gmair.model.drift;

import finley.gmair.model.Entity;

public class Equipment extends Entity {
    private String equipId;

    private String equipName;

    private double equipPrice;

    public Equipment() {
        super();
    }

    public Equipment(String equipName, double equipPrice) {
        this();
        this.equipName = equipName;
        this.equipPrice = equipPrice;
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

    public double getEquipPrice() {
        return equipPrice;
    }

    public void setEquipPrice(double equipPrice) {
        this.equipPrice = equipPrice;
    }
}
