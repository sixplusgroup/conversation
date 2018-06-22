package finley.gmair.model.machine;

import finley.gmair.model.Entity;

public class ControlOption extends Entity {
    private String controlId;

    private String optionName;

    private String optionComponent;

    public ControlOption() {
        super();
    }

    public ControlOption(String optionName, String optionComponent) {
        this();
        this.optionName = optionName;
        this.optionComponent = optionComponent;
    }

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
}
