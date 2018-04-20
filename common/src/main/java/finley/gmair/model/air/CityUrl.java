package finley.gmair.model.air;

import finley.gmair.model.Entity;

public class CityUrl extends Entity{

    private String cityId;
    private String cityUrl;

    public String getCityId() {
        return cityId;
    }

    public void setCityId(String cityId) {
        this.cityId = cityId;
    }

    public String getCityUrl() {
        return cityUrl;
    }

    public void setCityUrl(String cityUrl) {
        this.cityUrl = cityUrl;
    }

    public CityUrl(String cityId, String cityUrl) {
        this.cityId = cityId;
        this.cityUrl = cityUrl;
    }
}
