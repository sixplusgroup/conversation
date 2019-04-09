package nbiot.finley.model;

public class UserInfo extends Entity {
    private String userInfoId;

    private String miniOpenId;

    private String userMobile;

    private String username;

    public UserInfo() {
        super();
    }

    public UserInfo(String miniOpenId, String userMobile, String username) {
        this();
        this.miniOpenId = miniOpenId;
        this.userMobile = userMobile;
        this.username = username;
    }

    public String getUserInfoId() {
        return userInfoId;
    }

    public void setUserInfoId(String userInfoId) {
        this.userInfoId = userInfoId;
    }

    public String getMiniOpenId() {
        return miniOpenId;
    }

    public void setMiniOpenId(String miniOpenId) {
        this.miniOpenId = miniOpenId;
    }

    public String getUserMobile() {
        return userMobile;
    }

    public void setUserMobile(String userMobile) {
        this.userMobile = userMobile;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }
}
