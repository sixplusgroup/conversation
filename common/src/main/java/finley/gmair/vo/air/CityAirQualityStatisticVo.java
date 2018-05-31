package finley.gmair.vo.air;

public class CityAirQualityStatisticVo {
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

    public CityAirQualityStatisticVo(String cityId, double pm25) {
        this.cityId = cityId;
        this.pm25 = pm25;
    }
}
