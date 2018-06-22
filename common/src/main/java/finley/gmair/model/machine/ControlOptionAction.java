package finley.gmair.model.machine;

import finley.gmair.model.Entity;

public class ControlOptionAction extends Entity {
    private String valueId;

    private String controlId;

    private String modelId;

    private String actionName;

    private String actionOperator;

    public ControlOptionAction() {
        super();
    }

    public ControlOptionAction(String controlId, String modelId, String actionName, String actionOperator) {
        this();
        this.controlId = controlId;
        this.modelId = modelId;
        this.actionName = actionName;
        this.actionOperator = actionOperator;
    }

    public String getValueId() {
        return valueId;
    }

    public void setValueId(String valueId) {
        this.valueId = valueId;
    }

    public String getControlId() {
        return controlId;
    }

    public void setControlId(String controlId) {
        this.controlId = controlId;
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
