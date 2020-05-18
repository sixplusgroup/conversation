package finley.gmair.model.installation;

import finley.gmair.model.Entity;

public class SnapshotChangeMachine extends Entity {

    private String snapshotId;

    private String assignId;

    private String oldCodeValue;

    private String newCodeValue;

    private String picturePath;

    private boolean wifiConfigured;

    private String description;

    private String wayBillNumber;

    public SnapshotChangeMachine() {
        super();
    }

    public SnapshotChangeMachine(String assignId, String oldCodeValue, String newCodeValue, String picturePath, boolean wifiConfigured, String description, String wayBillNumber) {
        this.assignId = assignId;
        this.oldCodeValue = oldCodeValue;
        this.newCodeValue = newCodeValue;
        this.picturePath = picturePath;
        this.wifiConfigured = wifiConfigured;
        this.description = description;
        this.wayBillNumber = wayBillNumber;
    }

    public String getSnapshotId() {
        return snapshotId;
    }

    public void setSnapshotId(String snapshotId) {
        this.snapshotId = snapshotId;
    }

    public String getAssignId() {
        return assignId;
    }

    public void setAssignId(String assignId) {
        this.assignId = assignId;
    }

    public String getOldCodeValue() { return oldCodeValue; }

    public void setOldCodeValue(String oldCodeValue) { this.oldCodeValue = oldCodeValue; }

    public String getNewCodeValue() { return newCodeValue; }

    public void setNewCodeValue(String newCodeValue) { this.newCodeValue = newCodeValue; }

    public String getPicturePath() {
        return picturePath;
    }

    public void setPicturePath(String picturePath) {
        this.picturePath = picturePath;
    }

    public boolean isWifiConfigured() {
        return wifiConfigured;
    }

    public void setWifiConfigured(boolean wifiConfigured) {
        this.wifiConfigured = wifiConfigured;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getWayBillNumber() { return wayBillNumber; }

    public void setWayBillNumber(String wayBillNumber) { this.wayBillNumber = wayBillNumber; }
}
