package finley.gmair.model.installation;

import finley.gmair.model.Entity;

public class SnapshotDisassemble extends Entity {
    private String snapshotId;

    private String assignId;

    private String codeValue;

    private String picturePath;

    private String description;

    private String wayBillNumber;

    public SnapshotDisassemble() {
        super();
    }

    public SnapshotDisassemble(String assignId, String codeValue, String picturePath, String description, String wayBillNumber) {
        this.assignId = assignId;
        this.codeValue = codeValue;
        this.picturePath = picturePath;
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

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getWayBillNumber() { return wayBillNumber; }

    public void setWayBillNumber(String wayBillNumber) { this.wayBillNumber = wayBillNumber; }
}
