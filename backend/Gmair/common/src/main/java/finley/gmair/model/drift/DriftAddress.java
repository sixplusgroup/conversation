package finley.gmair.model.drift;

import finley.gmair.model.Entity;

public class DriftAddress extends Entity {
    private String addressId;

    private String consumerId;

    private String consignee;

    private String province;

    private String city;

    private String district;

    private String addressDetail;

    private String phone;

    private int defaultAddress;

    public DriftAddress() {
        super();
    }

    public DriftAddress(String consumerId, String consignee, String province, String city, String district, String addressDetail, String phone, int defaultAddress) {
        this();
        this.consumerId = consumerId;
        this.consignee = consignee;
        this.province = province;
        this.city = city;
        this.district = district;
        this.addressDetail = addressDetail;
        this.phone = phone;
        this.defaultAddress = defaultAddress;
    }

    public String getAddressId() {
        return addressId;
    }

    public void setAddressId(String addressId) {
        this.addressId = addressId;
    }

    public String getConsumerId() {
        return consumerId;
    }

    public void setConsumerId(String consumerId) {
        this.consumerId = consumerId;
    }

    public String getConsignee() {
        return consignee;
    }

    public void setConsignee(String consignee) {
        this.consignee = consignee;
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

    public String getAddressDetail() {
        return addressDetail;
    }

    public void setAddressDetail(String addressDetail) {
        this.addressDetail = addressDetail;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public int getDefaultAddress() {
        return defaultAddress;
    }

    public void setDefaultAddress(int defaultAddress) {
        this.defaultAddress = defaultAddress;
    }
}
