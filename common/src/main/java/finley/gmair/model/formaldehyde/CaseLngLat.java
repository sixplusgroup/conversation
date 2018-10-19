package finley.gmair.model.formaldehyde;

import finley.gmair.model.Entity;

public class CaseLngLat extends Entity {
    private String recordId;
    private String caseId;
    private double longitude;
    private double latitude;

    public CaseLngLat() {
        super();
    }

    public CaseLngLat(String caseId, double longitude, double latitude) {
        super();
        this.caseId = caseId;
        this.longitude = longitude;
        this.latitude = latitude;
    }

    public String getRecordId() {
        return recordId;
    }

    public void setRecordId(String recordId) {
        this.recordId = recordId;
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
