package finley.gmair.model.wechat;

import finley.gmair.model.Entity;

public class WechatUser extends Entity {
    private String userId;

    private String wechatId;

    private String nickName;

    private String userSex;

    private String userCity;

    private String userProvince;

    public WechatUser() {
        super();
    }

    public WechatUser(String userId, String wechatId, String nickName, String userSex, String userCity, String userProvince) {
        this();
        this.userId = userId;
        this.wechatId = wechatId;
        this.nickName = nickName;
        this.userSex = userSex;
        this.userCity = userCity;
        this.userProvince = userProvince;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public String getWechatId() {
        return wechatId;
    }

    public void setWechatId(String wechatId) {
        this.wechatId = wechatId;
    }

    public String getNickName() {
        return nickName;
    }

    public void setNickName(String nickName) {
        this.nickName = nickName;
    }

    public String getUserSex() {
        return userSex;
    }

    public void setUserSex(String userSex) {
        this.userSex = userSex;
    }

    public String getUserCity() {
        return userCity;
    }

    public void setUserCity(String userCity) {
        this.userCity = userCity;
    }

    public String getUserProvince() {
        return userProvince;
    }

    public void setUserProvince(String userProvince) {
        this.userProvince = userProvince;
    }
}