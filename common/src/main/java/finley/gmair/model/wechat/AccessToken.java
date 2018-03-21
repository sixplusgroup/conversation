package finley.gmair.model.wechat;

import finley.gmair.model.Entity;

public class AccessToken extends Entity {
    private String accessToken;

    public AccessToken() {
        super();
    }

    public AccessToken(String accessToken) {
        this();
        this.accessToken = accessToken;
    }

    public String getAccessToken() {
        return accessToken;
    }

    public void setAccessToken(String accessToken) {
        this.accessToken = accessToken;
    }
}