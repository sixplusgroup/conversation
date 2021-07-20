package finley.gmair.model.drift;

import finley.gmair.model.Entity;

public class Equipment extends Entity {
    private String equipId;

    private String equipName;

    private double equipPrice;

    private String text;

    private String url;

    private String detailUrl;

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

    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public String getDetailUrl() {
        return detailUrl;
    }

    public void setDetailUrl(String detailUrl) {
        this.detailUrl = detailUrl;
    }
}
