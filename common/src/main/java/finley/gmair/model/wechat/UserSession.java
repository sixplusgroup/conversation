package finley.gmair.model.wechat;

import finley.gmair.model.Entity;

public class UserSession extends Entity {
    private String openId;

    private String sessionKey;

    public UserSession() {
        super();
    }

    public UserSession(String openId, String sessionKey) {
        this();
        this.openId = openId;
        this.sessionKey = sessionKey;
    }

    public String getOpenId() {
        return openId;
    }

    public void setOpenId(String openId) {
        this.openId = openId;
    }

    public String getSessionKey() {
        return sessionKey;
    }

    public void setSessionKey(String sessionKey) {
        this.sessionKey = sessionKey;
    }
}
