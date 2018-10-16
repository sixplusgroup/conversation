package finley.gmair.model.map;

public class MachineLoc {
    private String consumerId;
    private String bindName;
    private String machineId;
    private String codeValue;
    private double longitude;
    private double latitude;

    public MachineLoc(String consumerId, String bindName, String machineId, String codeValue, double longitude, double latitude) {
        this.consumerId = consumerId;
        this.bindName = bindName;
        this.machineId = machineId;
        this.codeValue = codeValue;
        this.longitude = longitude;
        this.latitude = latitude;
    }

    public String getConsumerId() {
        return consumerId;
    }

    public void setConsumerId(String consumerId) {
        this.consumerId = consumerId;
    }

    public String getBindName() {
        return bindName;
    }

    public void setBindName(String bindName) {
        this.bindName = bindName;
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

    public double getLongitude() {
        return longitude;
    }

    public void setLongitude(double longitude) {
        this.longitude = longitude;
    }

    public double getLatitude() {
        return latitude;
    }

    public void setLatitude(double latitude) {
        this.latitude = latitude;
    }
}
