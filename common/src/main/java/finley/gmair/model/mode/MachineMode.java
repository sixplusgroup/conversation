package finley.gmair.model.mode;

import finley.gmair.model.Entity;

public class MachineMode extends Entity {
    private String modeId;
    private String machineId;
    private ModeType modeType;
    private String modeName;
    private boolean modeStatus;

    public MachineMode(){
        super();
    }

    public MachineMode(String machineId, ModeType modeType, String modeName, boolean modeStatus) {
        super();
        this.machineId = machineId;
        this.modeType = modeType;
        this.modeName = modeName;
        this.modeStatus = modeStatus;
    }

    public MachineMode(String modeId, String machineId, ModeType modeType, String modeName, boolean modeStatus) {
        this.modeId = modeId;
        this.machineId = machineId;
        this.modeType = modeType;
        this.modeName = modeName;
        this.modeStatus = modeStatus;
    }

    public String getModeId() {
        return modeId;
    }

    public void setModeId(String modeId) {
        this.modeId = modeId;
    }

    public String getMachineId() {
        return machineId;
    }

    public void setMachineId(String machineId) {
        this.machineId = machineId;
    }

    public ModeType getModeType() {
        return modeType;
    }

    public void setModeType(ModeType modeType) {
        this.modeType = modeType;
    }

    public String getModeName() {
        return modeName;
    }

    public void setModeName(String modeName) {
        this.modeName = modeName;
    }

    public boolean isModeStatus() {
        return modeStatus;
    }

    public void setModeStatus(boolean modeStatus) {
        this.modeStatus = modeStatus;
    }
}
