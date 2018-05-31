package finley.gmair.model.machine;

import finley.gmair.model.Entity;

/**
 * @author shenghaohu@hshvip.com
 * @date 2018/5/31
 */
public class PreBindCode extends Entity {
    private String bindId;

    private String machineId;

    private String codeValue;

    public PreBindCode() {
        super();
    }

    public PreBindCode(String machineId, String codeValue) {
        this();
        this.machineId = machineId;
        this.codeValue = codeValue;
    }

    public String getBindId() {
        return bindId;
    }

    public void setBindId(String bindId) {
        this.bindId = bindId;
    }

    public String getMachineId() {
        return machineId;
    }

    public void setMachineId(String machineId) {
        this.machineId = machineId;
    }

    public String getCodeValue() {
        return codeValue;
    }

    public void setCodeValue(String codeValue) {
        this.codeValue = codeValue;
    }
}