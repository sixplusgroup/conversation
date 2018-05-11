package finley.gmair.model.installation;

import finley.gmair.model.Entity;

public class SnapshotLoc extends Entity {
    private String locationId;
    private String snapshotId;
    private double locationLng;
    private double locationLat;
    private String locationPlace;

    public SnapshotLoc() {super();}
    public SnapshotLoc(String snapshotId, double locationLng, double locationLat, String locationPlace) {
        this();
        this.snapshotId = snapshotId;
        this.locationLng = locationLng;
        this.locationLat = locationLat;
        this.locationPlace = locationPlace;
    }

    public String getLocationId() {
        return locationId;
    }

    public void setLocationId(String locationId) {
        this.locationId = locationId;
    }

    public String getSnapshotId() {
        return snapshotId;
    }

    public void setSnapshotId(String snapshotId) {
        this.snapshotId = snapshotId;
    }

    public double getLocationLng() {
        return locationLng;
    }

    public void setLocationLng(double locationLng) {
        this.locationLng = locationLng;
    }

    public double getLocationLat() {
        return locationLat;
    }

    public void setLocationLat(double locationLat) {
        this.locationLat = locationLat;
    }

    public String getLocationPlace() {
        return locationPlace;
    }

    public void setLocationPlace(String locationPlace) {
        this.locationPlace = locationPlace;
    }
}
