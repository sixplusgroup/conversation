package finley.gmair.model.machine;

import finley.gmair.model.Entity;

public class ModelVolume extends Entity {
    private String configId;
    private String modelId;
    private int minVolume;
    private int maxVolume;
    public ModelVolume(){
        super();
    }

    public ModelVolume(String modelId, int minVolume, int maxVolume) {
        this.modelId = modelId;
        this.minVolume = minVolume;
        this.maxVolume = maxVolume;
    }

    public ModelVolume(int minVolume, int maxVolume) {
        this();
        this.minVolume = minVolume;
        this.maxVolume = maxVolume;
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

    public int getMinVolume() {
        return minVolume;
    }

    public void setMinVolume(int minVolume) {
        this.minVolume = minVolume;
    }

    public int getMaxVolume() {
        return maxVolume;
    }

    public void setMaxVolume(int maxVolume) {
        this.maxVolume = maxVolume;
    }
}
