package finley.gmair.model.drift;

import finley.gmair.model.Entity;

import java.util.Date;

public class Reservation extends Entity {
    private String reservationId;

    private String consumerId;

    private String goodsId;

    private Date expected;

    private int interval;

    private String consigneeName;

    private String consigneePhone;

    private String consigneeAddress;

    private String provinceId;

    private String cityId;

    private String testTarget;

    public Reservation() {
        super();
    }

    public Reservation(String consumerId, String goodsId, Date expected, int interval, String consigneeName, String consigneePhone, String consigneeAddress, String provinceId, String cityId, String testTarget) {
        this();
        this.consumerId = consumerId;
        this.goodsId = goodsId;
        this.expected = expected;
        this.interval = interval;
        this.consigneeName = consigneeName;
        this.consigneePhone = consigneePhone;
        this.consigneeAddress = consigneeAddress;
        this.provinceId = provinceId;
        this.cityId = cityId;
        this.testTarget = testTarget;
    }

    public String getReservationId() {
        return reservationId;
    }

    public void setReservationId(String reservationId) {
        this.reservationId = reservationId;
    }

    public String getConsumerId() {
        return consumerId;
    }

    public void setConsumerId(String consumerId) {
        this.consumerId = consumerId;
    }

    public String getGoodsId() {
        return goodsId;
    }

    public void setGoodsId(String goodsId) {
        this.goodsId = goodsId;
    }

    public Date getExpected() {
        return expected;
    }

    public void setExpected(Date expected) {
        this.expected = expected;
    }

    public int getInterval() {
        return interval;
    }

    public void setInterval(int interval) {
        this.interval = interval;
    }

    public String getConsigneeName() {
        return consigneeName;
    }

    public void setConsigneeName(String consigneeName) {
        this.consigneeName = consigneeName;
    }

    public String getConsigneePhone() {
        return consigneePhone;
    }

    public void setConsigneePhone(String consigneePhone) {
        this.consigneePhone = consigneePhone;
    }

    public String getConsigneeAddress() {
        return consigneeAddress;
    }

    public void setConsigneeAddress(String consigneeAddress) {
        this.consigneeAddress = consigneeAddress;
    }

    public String getProvinceId() {
        return provinceId;
    }

    public void setProvinceId(String provinceId) {
        this.provinceId = provinceId;
    }

    public String getCityId() {
        return cityId;
    }

    public void setCityId(String cityId) {
        this.cityId = cityId;
    }

    public String getTestTarget() {
        return testTarget;
    }

    public void setTestTarget(String testTarget) {
        this.testTarget = testTarget;
    }
}
