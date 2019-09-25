package finley.gmair.form.drift;

public class AttachmentForm {
    private String equipId;

    private String attachName;

    private String meal;

    private int attachSingle;

    private double attachPrice;

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

    public String getMeal() {
        return meal;
    }

    public void setMeal(String meal) {
        this.meal = meal;
    }

    public int getAttachSingle() {
        return attachSingle;
    }

    public void setAttachSingle(int attachSingle) {
        this.attachSingle = attachSingle;
    }

    public double getAttachPrice() {
        return attachPrice;
    }

    public void setAttachPrice(double attachPrice) {
        this.attachPrice = attachPrice;
    }
}
