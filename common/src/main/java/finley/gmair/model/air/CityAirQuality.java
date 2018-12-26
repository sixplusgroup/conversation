package finley.gmair.model.air;

import java.sql.Timestamp;

public class CityAirQuality extends AirQuality {

    private String cityId;
    private Timestamp recordTime;

    public String getCityId() {
        return cityId;
    }

    public void setCityId(String cityId) {
        this.cityId = cityId;
    }

    public Timestamp getRecordTime() {
        return recordTime;
    }

    public void setRecordTime(Timestamp recordTime) {
        this.recordTime = recordTime;
    }
}
