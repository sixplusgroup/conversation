package finley.gmair.vo.machine;

import java.util.List;

public class ControlOptionVo {
    private String controlId;

    private String optionName;

    private String optionComponent;

    private String modelId;

    private List<ControlOptionActionVo> actions;

    private boolean blockFlag;

    private String createAt;

    public String getControlId() {
        return controlId;
    }

    public void setControlId(String controlId) {
        this.controlId = controlId;
    }

    public String getOptionName() {
        return optionName;
    }

    public void setOptionName(String optionName) {
        this.optionName = optionName;
    }

    public String getOptionComponent() {
        return optionComponent;
    }

    public void setOptionComponent(String optionComponent) {
        this.optionComponent = optionComponent;
    }

    public String getModelId() {
        return modelId;
    }

    public void setModelId(String modelId) {
        this.modelId = modelId;
    }

    public List<ControlOptionActionVo> getActions() {
        return actions;
    }

    public void setActions(List<ControlOptionActionVo> actions) {
        this.actions = actions;
    }

    public boolean isBlockFlag() {
        return blockFlag;
    }

    public void setBlockFlag(boolean blockFlag) {
        this.blockFlag = blockFlag;
    }

    public String getCreateAt() {
        return createAt;
    }

    public void setCreateAt(String createAt) {
        this.createAt = createAt;
    }
}
