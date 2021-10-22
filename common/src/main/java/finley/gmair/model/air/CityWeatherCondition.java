package finley.gmair.model.air;

import java.sql.Timestamp;

public class CityWeatherCondition extends WeatherCondition {
    private String cityId;

    public CityWeatherCondition() {
        super();
    }

    public CityWeatherCondition(String cityId, String condition, int conditionId, int humidity, int icon, int pressure, int realFeel, Timestamp sunRise, Timestamp sunSet, int temp, String tips, int uvi, int vis, int windDegrees, String windDir, int windLevel, double windSpeed, Timestamp updateTime) {
        super(condition, conditionId, humidity, icon, pressure, realFeel, sunRise, sunSet, temp, tips, uvi, vis, windDegrees, windDir, windLevel, windSpeed, updateTime);
        this.cityId = cityId;
    }

    public String getCityId() {
        return cityId;
    }

    public void setCityId(String cityId) {
        this.cityId = cityId;
    }

}
