package finley.gmair.model.config;

import finley.gmair.model.Entity;

/**
 * @ClassName: GmairConfig
 * @Description: TODO
 * @Author fan
 * @Date 2019/4/26 3:58 PM
 */
public class GmairConfig extends Entity {
    private String configId;

    private String configComp;

    private boolean status;

    public GmairConfig() {
        super();
    }

    public GmairConfig(String configComp, boolean status) {
        this();
        this.configComp = configComp;
        this.status = status;
    }

    public String getConfigId() {
        return configId;
    }

    public void setConfigId(String configId) {
        this.configId = configId;
    }

    public String getConfigComp() {
        return configComp;
    }

    public void setConfigComp(String configComp) {
        this.configComp = configComp;
    }

    public boolean isStatus() {
        return status;
    }

    public void setStatus(boolean status) {
        this.status = status;
    }
}
