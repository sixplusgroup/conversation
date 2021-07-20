package finley.gmair.model.minivoice;

import java.util.List;

public class ResponsePayload {

    String deviceId;

    String cityId;

    List<Attribute> attributes;

    List<Property> properties;

    String errorCode;

    String message;

    Condition condition;

    List<Forecast> forecast;

    public ResponsePayload() {
    }

    /**
     * 用于设备控制的响应
     *
     * @param properties properties
     */
    public ResponsePayload(List<Property> properties) {
        this.properties = properties;
    }

    /**
     * 用于设备查询的响应
     *
     * @param deviceId   deviceId
     * @param attributes attributes
     */
    public ResponsePayload(String deviceId, List<Attribute> attributes) {
        this.deviceId = deviceId;
        this.attributes = attributes;
    }

    /**
     * 用于天气实况查询的响应
     *
     * @param cityId    cityId
     * @param condition condition
     */
    public ResponsePayload(String cityId, Condition condition) {
        this.cityId = cityId;
        this.condition = condition;
    }

    public ResponsePayload(String cityId, String errorCode, String message) {
        this.cityId = cityId;
        this.errorCode = errorCode;
        this.message = message;
    }

    public String getDeviceId() {
        return deviceId;
    }

    public void setDeviceId(String deviceId) {
        this.deviceId = deviceId;
    }

    public String getCityId() {
        return cityId;
    }

    public void setCityId(String cityId) {
        this.cityId = cityId;
    }

    public List<Property> getProperties() {
        return properties;
    }

    public void setProperties(List<Property> properties) {
        this.properties = properties;
    }

    public String getErrorCode() {
        return errorCode;
    }

    public void setErrorCode(String errorCode) {
        this.errorCode = errorCode;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public List<Attribute> getAttributes() {
        return attributes;
    }

    public void setAttributes(List<Attribute> attributes) {
        this.attributes = attributes;
    }

    public Condition getCondition() {
        return condition;
    }

    public void setCondition(Condition condition) {
        this.condition = condition;
    }

    public List<Forecast> getForecast() {
        return forecast;
    }

    public void setForecast(List<Forecast> forecast) {
        this.forecast = forecast;
    }

}
