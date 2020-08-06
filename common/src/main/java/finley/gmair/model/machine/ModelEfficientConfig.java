package finley.gmair.model.machine;

import finley.gmair.model.Entity;

public class ModelEfficientConfig extends Entity {

    private String configId;

    private String modelId;

    private int firstRemind;

    private int secondRemind;

    private int resetHour;

    public ModelEfficientConfig() {
        super();
    }

    public ModelEfficientConfig(String configId, String modelId, int firstRemind, int secondRemind, int resetHour) {
        super();
        this.configId = configId;
        this.modelId = modelId;
        this.firstRemind = firstRemind;
        this.secondRemind = secondRemind;
        this.resetHour = resetHour;
    }

    public String getConfigId() {
        return configId;
    }

    public void setConfigId(String configId) {
        this.configId = configId;
    }

    public String getModelId() {
        return modelId;
    }

    public void setModelId(String modelId) {
        this.modelId = modelId;
    }

    public int getFirstRemind() {
        return firstRemind;
    }

    public void setFirstRemind(int firstRemind) {
        this.firstRemind = firstRemind;
    }

    public int getSecondRemind() {
        return secondRemind;
    }

    public void setSecondRemind(int secondRemind) {
        this.secondRemind = secondRemind;
    }

    public int getResetHour() {
        return resetHour;
    }

    public void setResetHour(int resetHour) {
        this.resetHour = resetHour;
    }
}
