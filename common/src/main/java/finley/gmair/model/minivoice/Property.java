package finley.gmair.model.minivoice;

public class Property {

    private String deviceId;

    private String name;

    private String value;

    public Property() {
        super();
    }

    public Property(String deviceId, String name, String value) {
        this.deviceId = deviceId;
        this.name = name;
        this.value = value;
    }

    public String getDeviceId() {
        return deviceId;
    }

    public void setDeviceId(String deviceId) {
        this.deviceId = deviceId;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getValue() {
        return value;
    }

    public void setValue(String value) {
        this.value = value;
    }

    @Override
    public String toString() {
        return "Property{" +
                "deviceId='" + deviceId + '\'' +
                ", name='" + name + '\'' +
                ", value='" + value + '\'' +
                '}';
    }

}
