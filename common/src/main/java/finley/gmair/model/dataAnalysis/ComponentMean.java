package finley.gmair.model.dataAnalysis;

import finley.gmair.model.Entity;

public class ComponentMean extends Entity {
    private String recordId;
    private String dateIndex;
    private String component;
    private int componentTimes;
    private double componentMean;

    public ComponentMean(){super();}

    public ComponentMean(String dateIndex, String component, int componentTimes, double componentMean){
        this();
        this.dateIndex = dateIndex;
        this.component = component;
        this.componentTimes = componentTimes;
        this.componentMean = componentMean;
    }

    public String getRecordId() {
        return recordId;
    }

    public void setRecordId(String recordId) {
        this.recordId = recordId;
    }

    public String getDateIndex() {
        return dateIndex;
    }

    public void setDateIndex(String dateIndex) {
        this.dateIndex = dateIndex;
    }

    public String getComponent() {
        return component;
    }

    public void setComponent(String component) {
        this.component = component;
    }

    public int getComponentTimes() {
        return componentTimes;
    }

    public void setComponentTimes(int componentTimes) {
        this.componentTimes = componentTimes;
    }

    public double getComponentMean() {
        return componentMean;
    }

    public void setComponentMean(double componentMean) {
        this.componentMean = componentMean;
    }
}
