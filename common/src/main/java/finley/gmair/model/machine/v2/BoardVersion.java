package finley.gmair.model.machine.v2;

import finley.gmair.model.Entity;

public class BoardVersion extends Entity {
    private String machineId;

    private int version;

    public BoardVersion() {
        super();
        this.version = 0;
    }

    public BoardVersion(String machineId, int version) {
        super();
        this.machineId = machineId;
        this.version = version;
    }

    public String getMachineId() {
        return machineId;
    }

    public void setMachineId(String machineId) {
        this.machineId = machineId;
    }

    public int getVersion() {
        return version;
    }

    public void setVersion(int version) {
        this.version = version;
    }
}
