package finley.gmair.model.mqttManagement;

import finley.gmair.model.Entity;

public class Firmware extends Entity {
    private String firmwareId;
    private String firmwareVersion;
    private String firmwareLink;
    private String firmwareModel;

    public Firmware() {
        super();
    }

    public Firmware(String firmwareVersion, String firmwareLink, String firmwareModel) {
        this();
        this.firmwareVersion = firmwareVersion;
        this.firmwareLink = firmwareLink;
        this.firmwareModel = firmwareModel;
    }

    public String getFirmwareId() {
        return firmwareId;
    }

    public void setFirmwareId(String firmwareId) {
        this.firmwareId = firmwareId;
    }

    public String getFirmwareVersion() {
        return firmwareVersion;
    }

    public void setFirmwareVersion(String firmwareVersion) {
        this.firmwareVersion = firmwareVersion;
    }

    public String getFirmwareLink() {
        return firmwareLink;
    }

    public void setFirmwareLink(String firmwareLink) {
        this.firmwareLink = firmwareLink;
    }

    public String getFirmwareModel() {
        return firmwareModel;
    }

    public void setFirmwareModel(String firmwareModel) {
        this.firmwareModel = firmwareModel;
    }
}
