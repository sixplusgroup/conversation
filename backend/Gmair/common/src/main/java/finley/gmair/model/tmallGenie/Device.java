package finley.gmair.model.tmallGenie;

import java.util.List;

public class Device {

    String deviceId;

    String deviceName;

    String deviceType;

    String zone;

    String brand;

    String model;

    String icon;

    List<Attribute> properties;

    List<String> actions;

    Extensions extensions;

    public Device() {
        super();
    }

    public Device(String deviceId, String deviceName, String deviceType, String zone, String brand, String model, String icon, List<Attribute> properties, List<String> actions, Extensions extensions) {
        this.deviceId = deviceId;
        this.deviceName = deviceName;
        this.deviceType = deviceType;
        this.zone = zone;
        this.brand = brand;
        this.model = model;
        this.icon = icon;
        this.properties = properties;
        this.actions = actions;
        this.extensions = extensions;
    }

    public String getDeviceId() {
        return deviceId;
    }

    public void setDeviceId(String deviceId) {
        this.deviceId = deviceId;
    }

    public String getDeviceName() {
        return deviceName;
    }

    public void setDeviceName(String deviceName) {
        this.deviceName = deviceName;
    }

    public String getDeviceType() {
        return deviceType;
    }

    public void setDeviceType(String deviceType) {
        this.deviceType = deviceType;
    }

    public String getZone() {
        return zone;
    }

    public void setZone(String zone) {
        this.zone = zone;
    }

    public String getBrand() {
        return brand;
    }

    public void setBrand(String brand) {
        this.brand = brand;
    }

    public String getModel() {
        return model;
    }

    public void setModel(String model) {
        this.model = model;
    }

    public String getIcon() {
        return icon;
    }

    public void setIcon(String icon) {
        this.icon = icon;
    }

    public List<Attribute> getProperties() {
        return properties;
    }

    public void setProperties(List<Attribute> properties) {
        this.properties = properties;
    }

    public List<String> getActions() {
        return actions;
    }

    public void setActions(List<String> actions) {
        this.actions = actions;
    }

    public Extensions getExtensions() {
        return extensions;
    }

    public void setExtensions(Extensions extensions) {
        this.extensions = extensions;
    }
}
