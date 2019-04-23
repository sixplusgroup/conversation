package finley.gmair.model.wechat;

import finley.gmair.model.Entity;

public class AccessToken extends Entity {
    private String appId;

    private String accessToken;

    public AccessToken() {
        super();
    }

    public AccessToken(String appId, String accessToken) {
        this();
        this.appId = appId;
        this.accessToken = accessToken;
    }

    public String getAppId() {
        return appId;
    }

    public void setAppId(String appId) {
        this.appId = appId;
    }

    public String getAccessToken() {
        return accessToken;
    }

    public void setAccessToken(String accessToken) {
        this.accessToken = accessToken;
    }
}