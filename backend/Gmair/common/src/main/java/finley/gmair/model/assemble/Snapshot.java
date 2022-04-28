package finley.gmair.model.assemble;

import finley.gmair.model.Entity;

public class Snapshot extends Entity {
    private String snapshotId;
    private String codeValue;
    private String snapshotPath;
    private boolean checkStatus;

    public Snapshot() {
        super();
    }

    public Snapshot(String codeValue, String snapshotPath, boolean checkStatus) {
        super();
        this.codeValue = codeValue;
        this.snapshotPath = snapshotPath;
        this.checkStatus = checkStatus;
    }

    public String getSnapshotId() {
        return snapshotId;
    }

    public void setSnapshotId(String snapshotId) {
        this.snapshotId = snapshotId;
    }

    public String getCodeValue() {
        return codeValue;
    }

    public void setCodeValue(String codeValue) {
        this.codeValue = codeValue;
    }

    public String getSnapshotPath() {
        return snapshotPath;
    }

    public void setSnapshotPath(String snapshotPath) {
        this.snapshotPath = snapshotPath;
    }

    public boolean isCheckStatus() {
        return checkStatus;
    }

    public void setCheckStatus(boolean checkStatus) {
        this.checkStatus = checkStatus;
    }
}
