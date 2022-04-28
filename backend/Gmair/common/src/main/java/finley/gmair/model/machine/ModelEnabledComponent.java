package finley.gmair.model.machine;

import finley.gmair.model.Entity;

public class ModelEnabledComponent extends Entity {
    private String mecId;
    private String modelId;
    private String componentName;

    public ModelEnabledComponent() {
        super();
    }

    public ModelEnabledComponent(String mecId, String modelId, String componentName) {
        super();
        this.mecId = mecId;
        this.modelId = modelId;
        this.componentName = componentName;
    }

    public String getMecId() {
        return mecId;
    }

    public void setMecId(String mecId) {
        this.mecId = mecId;
    }

    public String getModelId() {
        return modelId;
    }

    public void setModelId(String modelId) {
        this.modelId = modelId;
    }

    public String getComponentName() {
        return componentName;
    }

    public void setComponentName(String componentName) {
        this.componentName = componentName;
    }
}
