package finley.gmair.model.air;

import finley.gmair.model.Entity;

public class CityAirQualityStatistic extends Entity{
    private String cityId;
    private double pm25;

    public String getCityId() {
        return cityId;
    }

    public void setCityId(String cityId) {
        this.cityId = cityId;
    }

    public double getPm25() {
        return pm25;
    }

    public void setPm25(double pm25) {
        this.pm25 = pm25;
    }
}
