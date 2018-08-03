package finley.gmair.model.machine;

import finley.gmair.model.Entity;

public class ModelLight extends Entity {
    private String configId;
    private String modelId;
    private int minLight;
    private int maxLight;
    public ModelLight(){
        super();
    }

    public ModelLight(String modelId, int minLight, int maxLight) {
        this.modelId = modelId;
        this.minLight = minLight;
        this.maxLight = maxLight;
    }

    public ModelLight(int minLight, int maxLight) {
        this();
        this.minLight = minLight;
        this.maxLight = maxLight;
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

    public int getMinLight() {
        return minLight;
    }

    public void setMinLight(int minLight) {
        this.minLight = minLight;
    }

    public int getMaxLight() {
        return maxLight;
    }

    public void setMaxLight(int maxLight) {
        this.maxLight = maxLight;
    }
}
