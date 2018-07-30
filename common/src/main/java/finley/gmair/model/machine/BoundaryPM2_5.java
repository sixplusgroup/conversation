package finley.gmair.model.machine;

import finley.gmair.model.Entity;

public class BoundaryPM2_5 extends Entity {
    private String boundaryId;
    private String modelId;
    private double pm2_5;
    public BoundaryPM2_5(){
        super();
    }

    public BoundaryPM2_5(String modelId, double pm2_5) {
        super();
        this.modelId = modelId;
        this.pm2_5 = pm2_5;
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

    public double getPm2_5() {
        return pm2_5;
    }

    public void setPm2_5(double pm2_5) {
        this.pm2_5 = pm2_5;
    }
}
