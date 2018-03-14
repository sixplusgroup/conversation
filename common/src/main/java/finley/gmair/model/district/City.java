package finley.gmair.model.district;

import finley.gmair.model.Entity;

public class City extends Entity {
    private String cityId;

    private String provinceId;

    private String cityName;

    private String cityPinyin;

    public City() {
        super();
    }

    public City(String cityId, String provinceId, String cityName, String cityPinyin) {
        this();
        this.cityId = cityId;
        this.provinceId = provinceId;
        this.cityName = cityName;
        this.cityPinyin = cityPinyin;
    }

    public String getCityId() {
        return cityId;
    }

    public void setCityId(String cityId) {
        this.cityId = cityId;
    }

    public String getProvinceId() {
        return provinceId;
    }

    public void setProvinceId(String provinceId) {
        this.provinceId = provinceId;
    }

    public String getCityName() {
        return cityName;
    }

    public void setCityName(String cityName) {
        this.cityName = cityName;
    }

    public String getCityPinyin() {
        return cityPinyin;
    }

    public void setCityPinyin(String cityPinyin) {
        this.cityPinyin = cityPinyin;
    }
}