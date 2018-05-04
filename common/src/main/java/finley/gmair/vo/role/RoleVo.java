package finley.gmair.vo.role;

import java.sql.Timestamp;

public class RoleVo {
    private String roleId;
    private String roleName;
    private String roleDescription;
    private String blockFlag;
    private Timestamp createAt;

    public String getRoleId() {
        return roleId;
    }

    public void setRoleId(String roleId) {
        this.roleId = roleId;
    }

    public String getRoleName() {
        return roleName;
    }

    public void setRoleName(String roleName) {
        this.roleName = roleName;
    }

    public String getRoleDescription() {
        return roleDescription;
    }

    public void setRoleDescription(String roleDescription) {
        this.roleDescription = roleDescription;
    }

    public String getBlockFlag() {
        return blockFlag;
    }

    public void setBlockFlag(String blockFlag) {
        this.blockFlag = blockFlag;
    }

    public Timestamp getCreateTime() {
        return createAt;
    }

    public void setCreateTime(Timestamp createTime) {
        this.createAt = createTime;
    }
}
