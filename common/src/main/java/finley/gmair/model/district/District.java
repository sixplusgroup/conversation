package finley.gmair.model.district;

import finley.gmair.model.Entity;

public class District extends Entity {
    private String districtId;

    private String cityId;

    private String districtName;

    public District() {
        super();
    }

    public District(String districtId, String cityId, String districtName) {
        this();
        this.districtId = districtId;
        this.cityId = cityId;
        this.districtName = districtName;
    }

    public String getDistrictId() {
        return districtId;
    }

    public void setDistrictId(String districtId) {
        this.districtId = districtId;
    }

    public String getCityId() {
        return cityId;
    }

    public void setCityId(String cityId) {
        this.cityId = cityId;
    }

    public String getDistrictName() {
        return districtName;
    }

    public void setDistrictName(String districtName) {
        this.districtName = districtName;
    }
}