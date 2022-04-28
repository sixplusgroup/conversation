package finley.gmair.model.installation;

import finley.gmair.model.Entity;

public class Pic extends Entity {
    private String picId;
    private String picAddress;
    private String picMd5;
    private String snapshotId;
    private boolean copyFlag;
    private boolean occupied;
    public Pic(){super();}
    public Pic(String picAddress,String snapshotId){
        this();
        this.picAddress=picAddress;
        this.snapshotId=snapshotId;
    }

    public String getPicId() {
        return picId;
    }

    public void setPicId(String picId) {
        this.picId = picId;
    }

    public String getPicAddress() {
        return picAddress;
    }

    public void setPicAddress(String picAddress) {
        this.picAddress = picAddress;
    }

    public String getPicMd5() {
        return picMd5;
    }

    public void setPicMd5(String picMd5) {
        this.picMd5 = picMd5;
    }

    public String getSnapshotId() {
        return snapshotId;
    }

    public void setSnapshotId(String snapshotId) {
        this.snapshotId = snapshotId;
    }

    public boolean isCopyFlag() {
        return copyFlag;
    }

    public void setCopyFlag(boolean copyFlag) {
        this.copyFlag = copyFlag;
    }

    public boolean isOccupied() {
        return occupied;
    }

    public void setOccupied(boolean occupied) {
        this.occupied = occupied;
    }
}
