package finley.gmair.model.machine;

import finley.gmair.model.Entity;

/**
 * @author shenghaohu@hshvip.com
 * @date 2018/5/31
 */
public class IdleMachine extends Entity {
    private String machineId;

    public IdleMachine() {
        super();
    }

    public IdleMachine(String machineId) {
        this();
        this.machineId = machineId;
    }

    public String getMachineId() {
        return machineId;
    }

    public void setMachineId(String machineId) {
        this.machineId = machineId;
    }
}