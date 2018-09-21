package finley.gmair.model.assemble;

import finley.gmair.model.Entity;

public class Snapshot extends Entity {
    private String snapshotId;
    private String codeValue;
    private String snapshotPath;

    public Snapshot() {
        super();
    }

    public Snapshot(String codeValue, String snapshotPath) {
        super();
        this.codeValue = codeValue;
        this.snapshotPath = snapshotPath;
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
}
