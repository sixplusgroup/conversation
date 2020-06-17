package finley.gmair.model.installation;

import finley.gmair.model.Entity;

public class Snapshot extends Entity {

    private String snapshotId;

    private String assignId;

    private String codeValue;

    private String picturePath;

    private boolean wifiConfigured;

    private String installMethod;

    private String description;

    private int hole;

    public Snapshot() {
        super();
    }

    public Snapshot(String assignId, String codeValue, String picturePath, boolean wifiConfigured, String installMethod, String description, int hole) {
        this.assignId = assignId;
        this.codeValue = codeValue;
        this.picturePath = picturePath;
        this.wifiConfigured = wifiConfigured;
        this.installMethod = installMethod;
        this.description = description;
        this.hole = hole;
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

    public String getCodeValue() {
        return codeValue;
    }

    public void setCodeValue(String codeValue) {
        this.codeValue = codeValue;
    }

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

    public String getInstallMethod() {
        return installMethod;
    }

    public void setInstallMethod(String installMethod) {
        this.installMethod = installMethod;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public int isHole() { return hole; }

    public void setHole(int hole) { this.hole = hole; }
}
