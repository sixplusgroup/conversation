package finley.gmair.form.drift;

import finley.gmair.model.ordernew.TradeFrom;

public class DriftOrderForm {
    private String consumerId;

    private String activityId;

    private String equipId;

    private String consignee;

    private String phone;

    private String address;

    private String province;

    private String city;

    private String district;

    private String description;

    private String expectedDate;

    private int intervalDate;

    private String excode;

    private String attachItem;

    private TradeFrom tradeFrom;

    public String getConsumerId() {
        return consumerId;
    }

    public void setConsumerId(String consumerId) {
        this.consumerId = consumerId;
    }

    public String getActivityId() {
        return activityId;
    }

    public void setActivityId(String activityId) {
        this.activityId = activityId;
    }

    public String getEquipId() {
        return equipId;
    }

    public void setEquipId(String equipId) {
        this.equipId = equipId;
    }

    public String getConsignee() {
        return consignee;
    }

    public void setConsignee(String consignee) {
        this.consignee = consignee;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getProvince() {
        return province;
    }

    public void setProvince(String province) {
        this.province = province;
    }

    public String getCity() {
        return city;
    }

    public void setCity(String city) {
        this.city = city;
    }

    public String getDistrict() {
        return district;
    }

    public void setDistrict(String district) {
        this.district = district;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getExpectedDate() {
        return expectedDate;
    }

    public void setExpectedDate(String expectedDate) {
        this.expectedDate = expectedDate;
    }

    public int getIntervalDate() {
        return intervalDate;
    }

    public void setIntervalDate(int intervalDate) {
        this.intervalDate = intervalDate;
    }

    public String getExcode() {
        return excode;
    }

    public void setExcode(String excode) {
        this.excode = excode;
    }

    public String getAttachItem() {
        return attachItem;
    }

    public void setAttachItem(String attachItem) {
        this.attachItem = attachItem;
    }

    public TradeFrom getTradeFrom() {
        return tradeFrom;
    }

    public void setTradeFrom(TradeFrom tradeFrom) {
        this.tradeFrom = tradeFrom;
    }
}
