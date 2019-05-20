package finley.gmair.model.mqtt;

import finley.gmair.model.Entity;

public class Firmware extends Entity {
    private String firmwareId;
    private String firmwareVersion;
    private String firmwareLink;

    public Firmware() {
        super();
    }

    public Firmware(String firmwareVersion, String firmwareLink) {
        this();
        this.firmwareVersion = firmwareVersion;
        this.firmwareLink = firmwareLink;
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
}
