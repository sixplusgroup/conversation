package finley.gmair.model.minivoice;

import com.fasterxml.jackson.annotation.JsonInclude;

import java.util.List;

@JsonInclude(JsonInclude.Include.NON_NULL)
public class RequestPayload {

    String accessToken;

    String deviceId;

    String cityId;

    List<Property> properties;

    public RequestPayload() {
        super();
    }

    /**
     * 用于设备控制的请求
     *
     * @param accessToken
     * @param properties
     */
    public RequestPayload(String accessToken, List<Property> properties) {
        this.accessToken = accessToken;
        this.properties = properties;
    }

    /**
     * 用于查询的请求
     *
     * @param accessToken
     * @param Id
     */
    public RequestPayload(String accessToken, String Id) {
        this.accessToken = accessToken;

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

    @Override
    public String toString() {
        return "RequestPayload{" +
                "accessToken='" + accessToken + '\'' +
                ", deviceId='" + deviceId + '\'' +
                ", cityId='" + cityId + '\'' +
                ", properties=" + properties +
                '}';
    }

}
