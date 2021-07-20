package finley.gmair.model.fan;

import finley.gmair.model.Entity;

/**
 * @ClassName: FanConfig
 * @Description: TODO
 * @Author fan
 * @Date 2019/11/16 10:15 AM
 */
public class FanConfig extends Entity {
    private String configId;

    private String mac;

    private String firmware;

    private String serverip;

    private int serverport;

    public FanConfig() {
        super();
    }

    public FanConfig(String mac, String firmware, String serverip, int serverport) {
        this();
        this.mac = mac;
        this.firmware = firmware;
        this.serverip = serverip;
        this.serverport = serverport;
    }

    public String getConfigId() {
        return configId;
    }

    public void setConfigId(String configId) {
        this.configId = configId;
    }

    public String getMac() {
        return mac;
    }

    public void setMac(String mac) {
        this.mac = mac;
    }

    public String getFirmware() {
        return firmware;
    }

    public void setFirmware(String firmware) {
        this.firmware = firmware;
    }

    public String getServerip() {
        return serverip;
    }

    public void setServerip(String serverip) {
        this.serverip = serverip;
    }

    public int getServerport() {
        return serverport;
    }

    public void setServerport(int serverport) {
        this.serverport = serverport;
    }
}
