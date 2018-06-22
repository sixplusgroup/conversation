package finley.gmair.form.machine;

/**
 * @author shenghaohu@hshvip.com
 * @date 2018/6/22
 */
public class ControlOptionForm {
    private String optionName;

    private String optionComponent;

    private String modelId;

    private String actionName;

    private String actionOperator;

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
}