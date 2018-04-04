package finley.gmair.vo.machine;

import java.sql.Timestamp;

public class MachineInstallTypeVo {
    private String mitId;
    private String installType;
    private boolean blockFlag;
    private Timestamp createTime;

    public String getMitId() {
        return mitId;
    }

    public void setMitId(String mitId) {
        this.mitId = mitId;
    }

    public String getInstallType() {
        return installType;
    }

    public void setInstallType(String installType) {
        this.installType = installType;
    }

    public boolean isBlockFlag() {
        return blockFlag;
    }

    public void setBlockFlag(boolean blockFlag) {
        this.blockFlag = blockFlag;
    }

    public Timestamp getCreateTime() {
        return createTime;
    }

    public void setCreateTime(Timestamp createTime) {
        this.createTime = createTime;
    }
}
