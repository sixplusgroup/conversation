package finley.gmair.model.air;

import java.sql.Timestamp;

public class MonitorStationAirQuality extends AirQuality{
    private String stationId;
    private Timestamp recordTime;

    public String getStationId() {
        return stationId;
    }

    public void setStationId(String stationId) {
        this.stationId = stationId;
    }

    public Timestamp getRecordTime() {
        return recordTime;
    }

    public void setRecordTime(Timestamp recordTime) {
        this.recordTime = recordTime;
    }
}
