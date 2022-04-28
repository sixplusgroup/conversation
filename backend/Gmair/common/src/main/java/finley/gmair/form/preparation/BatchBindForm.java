package finley.gmair.form.preparation;

public class BatchBindForm {
    private String machineId;
    private String codeValue;
    private int version;

    public BatchBindForm(String machineId, String codeValue, int version) {
        this.machineId = machineId;
        this.codeValue = codeValue;
        this.version = version;
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

    public int getVersion() {
        return version;
    }

    public void setVersion(int version) {
        this.version = version;
    }
}
