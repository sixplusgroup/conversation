package finley.gmair.vo.location;

import finley.gmair.model.Entity;

import java.sql.Timestamp;

public class CityProvinceVo extends Entity {
    private String cityId;
    private String provinceId;
    private String cityName;
    private String cityPinyin;

    public CityProvinceVo() {
        super();
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
