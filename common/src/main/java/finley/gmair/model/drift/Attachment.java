package finley.gmair.model.drift;

import finley.gmair.model.Entity;

public class Attachment extends Entity {
    private String attachId;

    private String equipId;

    private String attachName;

    private double attachPrice;

    public Attachment() {
        super();
    }

    public Attachment(String equipId, String attachName, double attachPrice) {
        this();
        this.equipId = equipId;
        this.attachName = attachName;
        this.attachPrice = attachPrice;
    }

    public String getAttachId() {
        return attachId;
    }

    public void setAttachId(String attachId) {
        this.attachId = attachId;
    }

    public String getEquipId() {
        return equipId;
    }

    public void setEquipId(String equipId) {
        this.equipId = equipId;
    }

    public String getAttachName() {
        return attachName;
    }

    public void setAttachName(String attachName) {
        this.attachName = attachName;
    }

    public double getAttachPrice() {
        return attachPrice;
    }

    public void setAttachPrice(double attachPrice) {
        this.attachPrice = attachPrice;
    }
}