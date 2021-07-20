package finley.gmair.model.drift;

import com.alibaba.fastjson.JSONObject;
import finley.gmair.model.Entity;

public class DriftUser extends Entity {
    private String userId;

    private String openId;

    private String nickname;

    private String phone;

    private String province;

    private String city;

    private String country;

    private int userSex;

    private String avatarUrl;

    public DriftUser() {
        super();
    }

    public DriftUser(String openId, String nickname,  String province, String city, String country, int userSex, String avatarUrl) {
        this();
        this.openId = openId;
        this.nickname = nickname;
//        this.phone = phone;
        this.province = province;
        this.city = city;
        this.country = country;
        this.userSex = userSex;
        this.avatarUrl = avatarUrl;
    }

    public DriftUser(String openId, String nickname,JSONObject object) {
        this(openId, nickname , object.getString("province"),
                object.getString("city"), object.getString("country"), object.getInteger("gender"),
                object.getString("avatarUrl"));
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public String getOpenId() {
        return openId;
    }

    public void setOpenId(String openId) {
        this.openId = openId;
    }

    public String getNickname() {
        return nickname;
    }

    public void setNickname(String nickname) {
        this.nickname = nickname;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getProvince() {
        return province;
    }

    public void setProvince(String province) {
        this.province = province;
    }

    public String getCity() {
        return city;
    }

    public void setCity(String city) {
        this.city = city;
    }

    public String getCountry() {
        return country;
    }

    public void setCountry(String country) {
        this.country = country;
    }

    public int getUserSex() {
        return userSex;
    }

    public void setUserSex(int userSex) {
        this.userSex = userSex;
    }

    public String getAvatarUrl() {
        return avatarUrl;
    }

    public void setAvatarUrl(String avatarUrl) {
        this.avatarUrl = avatarUrl;
    }
}
