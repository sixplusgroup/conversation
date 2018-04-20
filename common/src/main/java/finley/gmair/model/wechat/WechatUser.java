package finley.gmair.model.wechat;

import com.alibaba.fastjson.JSONObject;
import finley.gmair.model.Entity;

public class WechatUser extends Entity {
    private String userId;

    private String wechatId;

    private String nickName;

    private String userSex;

    private String userCity;

    private String userProvince;

    private String userCountry;

    private String headimgUrl;

    private String userUnionid;

    public WechatUser() {
        super();
    }

    public WechatUser(String wechatId, String nickName, String userSex, String userCity, String userProvince, String userCountry, String headimgUrl, String userUnionid) {
        this();
        this.wechatId = wechatId;
        this.nickName = nickName;
        this.userSex = userSex;
        this.userCity = userCity;
        this.userProvince = userProvince;
        this.userCountry = userCountry;
        this.headimgUrl = headimgUrl;
        this.userUnionid = userUnionid;
    }

    public WechatUser(String openId, JSONObject object) {
        this(openId, object.getString("nickname"), object.getString("sex"),
                object.getString("city"), object.getString("province"),
                object.getString("country"), object.getString("headimgurl"),
                object.getString("unionid"));
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

    public String getUserCountry() {
        return userCountry;
    }

    public void setUserCountry(String userCountry) {
        this.userCountry = userCountry;
    }

    public String getHeadimgUrl() {
        return headimgUrl;
    }

    public void setHeadimgUrl(String headimgUrl) {
        this.headimgUrl = headimgUrl;
    }

    public String getUserUnionid() {
        return userUnionid;
    }

    public void setUserUnionid(String userUnionid) {
        this.userUnionid = userUnionid;
    }
}