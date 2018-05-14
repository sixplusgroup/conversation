package finley.gmair.model.air;

import finley.gmair.model.Entity;

public class ObscureCity extends Entity{
    private String ocId;
    private String cityName;
    private String cityId;

    public String getOcId() {
        return ocId;
    }

    public void setOcId(String ocId) {
        this.ocId = ocId;
    }

    public String getCityName() {
        return cityName;
    }

    public void setCityName(String cityName) {
        this.cityName = cityName;
    }

    public String getCityId() {
        return cityId;
    }

    public void setCityId(String cityId) {
        this.cityId = cityId;
    }
}
