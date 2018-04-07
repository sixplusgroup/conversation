package finley.gmair.model.air;

import finley.gmair.model.Entity;

public class MonitorStation extends Entity{
    private String stationId;
    private String belongCityId;
    private String stationName;

    public String getStationId() {
        return stationId;
    }

    public void setStationId(String stationId) {
        this.stationId = stationId;
    }

    public String getBelongCityId() {
        return belongCityId;
    }

    public void setBelongCityId(String belongCityId) {
        this.belongCityId = belongCityId;
    }

    public String getStationName() {
        return stationName;
    }

    public void setStationName(String stationName) {
        this.stationName = stationName;
    }
}
