package finley.gmair.model.aircheck;

import finley.gmair.model.Entity;

public class CaseLocation extends Entity {
    private String caseId;

    private double longitude;

    private double latitude;

    public CaseLocation() {
        super();
    }

    public CaseLocation(double longitude, double latitude) {
        this();
        this.longitude = longitude;
        this.latitude = latitude;
    }

    public String getCaseId() {
        return caseId;
    }

    public void setCaseId(String caseId) {
        this.caseId = caseId;
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
