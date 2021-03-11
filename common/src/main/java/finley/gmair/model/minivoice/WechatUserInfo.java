package finley.gmair.model.minivoice;

import finley.gmair.model.Entity;

/**
 * 微信用户信息
 */
public class WechatUserInfo extends Entity {

    String session;

    String openid;

    String sessionKey;

    String purePhoneNumber;

    public WechatUserInfo() {
    }

    public WechatUserInfo(String session) {
        this.session = session;
    }

    public WechatUserInfo(String session, String openid, String sessionKey) {
        this.session = session;
        this.openid = openid;
        this.sessionKey = sessionKey;
    }

    public String getSession() {
        return session;
    }

    public void setSession(String session) {
        this.session = session;
    }

    public String getOpenid() {
        return openid;
    }

    public void setOpenid(String openid) {
        this.openid = openid;
    }

    public String getSessionKey() {
        return sessionKey;
    }

    public void setSessionKey(String sessionKey) {
        this.sessionKey = sessionKey;
    }

    public String getPurePhoneNumber() {
        return purePhoneNumber;
    }

    public void setPurePhoneNumber(String purePhoneNumber) {
        this.purePhoneNumber = purePhoneNumber;
    }

    @Override
    public String toString() {
        return "WechatUserInfo{" +
                "session='" + session + '\'' +
                ", openid='" + openid + '\'' +
                ", sessionKey='" + sessionKey + '\'' +
                ", purePhoneNumber='" + purePhoneNumber + '\'' +
                ", createAt=" + createAt +
                '}';
    }

}
