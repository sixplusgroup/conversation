package finley.gmair.model.tmallGenie;

import java.util.List;

public class Payload {

    String accessToken;

    String deviceId;

    String deviceType;

    String errorCode;

    String message;

    String attribute;

    String value;

    Extensions extensions;

    List<Device> devices;

    /**
     * 用于设备发现响应的构造函数
     * @param devices 设备列表
     */
    public Payload(List<Device> devices) {
        this.devices = devices;
    }

    public Payload() {
        super();
    }

    public Payload(String deviceId) {
        this.deviceId = deviceId;
    }

    public Payload(String deviceId, String errorCode, String message) {
        this.deviceId = deviceId;
        this.errorCode = errorCode;
        this.message = message;
    }

    public Payload(String accessToken, String deviceId, String deviceType, String errorCode, String message, String attribute, String value, Extensions extensions) {
        this.accessToken = accessToken;
        this.deviceId = deviceId;
        this.deviceType = deviceType;
        this.errorCode = errorCode;
        this.message = message;
        this.attribute = attribute;
        this.value = value;
        this.extensions = extensions;
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

    public String getAccessToken() {
        return accessToken;
    }

    public void setAccessToken(String accessToken) {
        this.accessToken = accessToken;
    }

    public String getDeviceId() {
        return deviceId;
    }

    public void setDeviceId(String deviceId) {
        this.deviceId = deviceId;
    }

    public String getDeviceType() {
        return deviceType;
    }

    public void setDeviceType(String deviceType) {
        this.deviceType = deviceType;
    }

    public String getAttribute() {
        return attribute;
    }

    public void setAttribute(String attribute) {
        this.attribute = attribute;
    }

    public String getValue() {
        return value;
    }

    public void setValue(String value) {
        this.value = value;
    }

    public Extensions getExtensions() {
        return extensions;
    }

    public void setExtensions(Extensions extensions) {
        this.extensions = extensions;
    }

    public List<Device> getDevices() {
        return devices;
    }

    public void setDevices(List<Device> devices) {
        this.devices = devices;
    }

}
