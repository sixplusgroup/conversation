package finley.gmair.vo.air;

import java.sql.Timestamp;

public class CityAirPm25Vo {
    private String cityId;
    private double pm25;
    private Timestamp recordTime;

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

    public Timestamp getRecordTime() {
        return recordTime;
    }

    public void setRecordTime(Timestamp recordTime) {
        this.recordTime = recordTime;
    }
}
