package finley.gmair.model.preparation;

public class PreBindField {
    private String machineId;

    private int version;

    private String codeValue;

    public PreBindField() {
    }

    public PreBindField(String machineId, int version, String codeValue) {
        this();
        this.machineId = machineId;
        this.version = version;
        this.codeValue = codeValue;
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

    public String getCodeValue() {
        return codeValue;
    }

    public void setCodeValue(String codeValue) {
        this.codeValue = codeValue;
    }
}
