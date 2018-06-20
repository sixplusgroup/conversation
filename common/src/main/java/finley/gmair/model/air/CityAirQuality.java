package finley.gmair.model.air;

import finley.gmair.model.Entity;

import java.sql.Timestamp;

public class CityAirQuality extends AirQuality{

    private String cityId;
    private String url;
    private Timestamp recordTime;

    public String getCityId() {
        return cityId;
    }

    public void setCityId(String cityId) {
        this.cityId = cityId;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }


    public Timestamp getRecordTime() {
        return recordTime;
    }

    public void setRecordTime(Timestamp recordTime) {
        this.recordTime = recordTime;
    }
}
