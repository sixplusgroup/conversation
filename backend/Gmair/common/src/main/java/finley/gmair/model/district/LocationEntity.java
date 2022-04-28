package finley.gmair.model.district;

import finley.gmair.model.Entity;

public abstract class LocationEntity extends Entity {
    protected double longitude;

    protected double latitude;

    public LocationEntity() {
        super();
    }

    public LocationEntity(double longitude, double latitude) {
        this();
        this.longitude = longitude;
        this.latitude = latitude;
    }

    public double getLongitude() {
        return longitude;
    }

    public void setLongitude(double longitude) {
        this.longitude = longitude;
    }

    public double getLatitude() {
        return latitude;
    }

    public void setLatitude(double latitude) {
        this.latitude = latitude;
    }
}
