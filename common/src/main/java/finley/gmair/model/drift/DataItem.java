package finley.gmair.model.drift;

import finley.gmair.model.Entity;

public class DataItem extends Entity {

    private String itemId;

    private String reportId;

    private String position;

    private double area;

    private double data;

    public DataItem() {
        super();
    }

    public DataItem(String reportId, String position, double area, double data) {
        this();
        this.reportId = reportId;
        this.position = position;
        this.area = area;
        this.data = data;
    }

    public String getItemId() {
        return itemId;
    }

    public void setItemId(String itemId) {
        this.itemId = itemId;
    }

    public String getReportId() {
        return reportId;
    }

    public void setReportId(String reportId) {
        this.reportId = reportId;
    }

    public String getPosition() {
        return position;
    }

    public void setPosition(String position) {
        this.position = position;
    }

    public double getArea() {
        return area;
    }

    public void setArea(double area) {
        this.area = area;
    }

    public double getData() {
        return data;
    }

    public void setData(double data) {
        this.data = data;
    }
}
