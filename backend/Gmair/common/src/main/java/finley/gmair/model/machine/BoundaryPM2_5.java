package finley.gmair.model.machine;

import finley.gmair.model.Entity;

public class BoundaryPM2_5 extends Entity {
    private String boundaryId;
    private String modelId;
    private int pm2_5_info;
    private int pm2_5_warning;
    public BoundaryPM2_5(){
        super();
    }

    public BoundaryPM2_5(String modelId, int pm2_5_info, int pm2_5_warning) {
        this.modelId = modelId;
        this.pm2_5_info = pm2_5_info;
        this.pm2_5_warning = pm2_5_warning;
    }

    public String getBoundaryId() {
        return boundaryId;
    }

    public void setBoundaryId(String boundaryId) {
        this.boundaryId = boundaryId;
    }

    public String getModelId() {
        return modelId;
    }

    public void setModelId(String modelId) {
        this.modelId = modelId;
    }

    public int getPm2_5_info() {
        return pm2_5_info;
    }

    public void setPm2_5_info(int pm2_5_info) {
        this.pm2_5_info = pm2_5_info;
    }

    public int getPm2_5_warning() {
        return pm2_5_warning;
    }

    public void setPm2_5_warning(int pm2_5_warning) {
        this.pm2_5_warning = pm2_5_warning;
    }
}
