package finley.gmair.model.machine;

import finley.gmair.model.Entity;

public class MachineLoc extends Entity {
    private String bindId;
    private String consumerId;
    private String bindName;
    private String codeValue;
    private String machineId;
    private String provinceId;
    private String cityId;
    private String cityName;
    private double longitude;
    private double latitude;

    public MachineLoc() {
        super();
    }

    public MachineLoc(String bindId, String consumerId, String bindName, String codeValue, String machineId, String provinceId, String cityId, String cityName, double longitude, double latitude) {
        super();
        this.bindId = bindId;
        this.consumerId = consumerId;
        this.bindName = bindName;
        this.codeValue = codeValue;
        this.machineId = machineId;
        this.provinceId = provinceId;
        this.cityId = cityId;
        this.cityName = cityName;
        this.longitude = longitude;
        this.latitude = latitude;
    }

    public String getBindId() {
        return bindId;
    }

    public void setBindId(String bindId) {
        this.bindId = bindId;
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

    public String getCodeValue() {
        return codeValue;
    }

    public void setCodeValue(String codeValue) {
        this.codeValue = codeValue;
    }

    public String getMachineId() {
        return machineId;
    }

    public void setMachineId(String machineId) {
        this.machineId = machineId;
    }

    public String getProvinceId() {
        return provinceId;
    }

    public void setProvinceId(String provinceId) {
        this.provinceId = provinceId;
    }

    public String getCityId() {
        return cityId;
    }

    public void setCityId(String cityId) {
        this.cityId = cityId;
    }

    public String getCityName() {
        return cityName;
    }

    public void setCityName(String cityName) {
        this.cityName = cityName;
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
