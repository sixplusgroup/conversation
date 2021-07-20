package finley.gmair.model.installation;

import finley.gmair.model.Entity;

public class SnapshotSurvey extends Entity {
    private String snapshotId;

    private String assignId;

    private String picturePath;

    private String description;

    public SnapshotSurvey() {
        super();
    }

    public SnapshotSurvey(String assignId,String picturePath, String description) {
        this.assignId = assignId;
        this.picturePath = picturePath;
        this.description = description;
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
}
