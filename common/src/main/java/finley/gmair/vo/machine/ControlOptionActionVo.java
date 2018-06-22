package finley.gmair.vo.machine;

import java.sql.Timestamp;

public class ControlOptionActionVo {
    private String valueId;

    private String actionName;

    private String actionOperator;

    private boolean blockFlag;

    private Timestamp createAt;

    public String getValueId() {
        return valueId;
    }

    public void setValueId(String valueId) {
        this.valueId = valueId;
    }

    public String getActionName() {
        return actionName;
    }

    public void setActionName(String actionName) {
        this.actionName = actionName;
    }

    public String getActionOperator() {
        return actionOperator;
    }

    public void setActionOperator(String actionOperator) {
        this.actionOperator = actionOperator;
    }

    public boolean isBlockFlag() {
        return blockFlag;
    }

    public void setBlockFlag(boolean blockFlag) {
        this.blockFlag = blockFlag;
    }

    public Timestamp getCreateAt() {
        return createAt;
    }

    public void setCreateAt(Timestamp createAt) {
        this.createAt = createAt;
    }
}
