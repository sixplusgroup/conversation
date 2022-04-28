package finley.gmair.model.assemble;

import finley.gmair.model.Entity;

public class CheckRecord extends Entity {
    private String recordId;
    private String snapshotId;
    private boolean recordStatus;

    public CheckRecord() {
        super();
    }

    public CheckRecord(String snapshotId, boolean recordStatus) {
        super();
        this.snapshotId = snapshotId;
        this.recordStatus = recordStatus;
    }

    public String getRecordId() {
        return recordId;
    }

    public void setRecordId(String recordId) {
        this.recordId = recordId;
    }

    public String getSnapshotId() {
        return snapshotId;
    }

    public void setSnapshotId(String snapshotId) {
        this.snapshotId = snapshotId;
    }

    public boolean isRecordStatus() {
        return recordStatus;
    }

    public void setRecordStatus(boolean recordStatus) {
        this.recordStatus = recordStatus;
    }
}
