package finley.gmair.model.consumer;

import finley.gmair.model.Entity;

public class Address extends Entity{
    private String addressId;

    private String detail;

    private String province;

    private String city;

    private String district;

    private boolean preferred;

    public Address() {
        super();
        this.preferred = false;
    }

    public Address(String detail, String province, String city, String district) {
        this();
        this.detail = detail;
        this.province = province;
        this.city = city;
        this.district = district;
    }

    public String getAddressId() {
        return addressId;
    }

    public void setAddressId(String addressId) {
        this.addressId = addressId;
    }

    public String getDetail() {
        return detail;
    }

    public void setDetail(String detail) {
        this.detail = detail;
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

    public boolean isPreferred() {
        return preferred;
    }

    public void setPreferred(boolean preferred) {
        this.preferred = preferred;
    }
}
