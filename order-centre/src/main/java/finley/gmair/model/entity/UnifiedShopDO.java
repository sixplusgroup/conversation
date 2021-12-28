package finley.gmair.model.entity;

import java.util.Date;

public class UnifiedShopDO {
    private String shopId;

    private Integer platform;

    private String sid;

    private String channel;

    private String shopTitle;

    private Date startPullTime;

    private Date lastPullTime;

    private String appKey;

    private String appSecret;

    private String sessionKey;

    private Date authorizeTime;

    private Date expireTime;

    private Date sysCreateTime;

    private Date sysUpdateTime;

    private Boolean sysBlockFlag;

    public UnifiedShopDO(String shopId, Integer platform, String sid, String channel, String shopTitle, Date startPullTime, Date lastPullTime, String appKey, String appSecret, String sessionKey, Date authorizeTime, Date expireTime, Date sysCreateTime, Date sysUpdateTime, Boolean sysBlockFlag) {
        this.shopId = shopId;
        this.platform = platform;
        this.sid = sid;
        this.channel = channel;
        this.shopTitle = shopTitle;
        this.startPullTime = startPullTime;
        this.lastPullTime = lastPullTime;
        this.appKey = appKey;
        this.appSecret = appSecret;
        this.sessionKey = sessionKey;
        this.authorizeTime = authorizeTime;
        this.expireTime = expireTime;
        this.sysCreateTime = sysCreateTime;
        this.sysUpdateTime = sysUpdateTime;
        this.sysBlockFlag = sysBlockFlag;
    }

    public UnifiedShopDO() {
        super();
    }

    public String getShopId() {
        return shopId;
    }

    public void setShopId(String shopId) {
        this.shopId = shopId == null ? null : shopId.trim();
    }

    public Integer getPlatform() {
        return platform;
    }

    public void setPlatform(Integer platform) {
        this.platform = platform;
    }

    public String getSid() {
        return sid;
    }

    public void setSid(String sid) {
        this.sid = sid == null ? null : sid.trim();
    }

    public String getChannel() {
        return channel;
    }

    public void setChannel(String channel) {
        this.channel = channel == null ? null : channel.trim();
    }

    public String getShopTitle() {
        return shopTitle;
    }

    public void setShopTitle(String shopTitle) {
        this.shopTitle = shopTitle == null ? null : shopTitle.trim();
    }

    public Date getStartPullTime() {
        return startPullTime;
    }

    public void setStartPullTime(Date startPullTime) {
        this.startPullTime = startPullTime;
    }

    public Date getLastPullTime() {
        return lastPullTime;
    }

    public void setLastPullTime(Date lastPullTime) {
        this.lastPullTime = lastPullTime;
    }

    public String getAppKey() {
        return appKey;
    }

    public void setAppKey(String appKey) {
        this.appKey = appKey == null ? null : appKey.trim();
    }

    public String getAppSecret() {
        return appSecret;
    }

    public void setAppSecret(String appSecret) {
        this.appSecret = appSecret == null ? null : appSecret.trim();
    }

    public String getSessionKey() {
        return sessionKey;
    }

    public void setSessionKey(String sessionKey) {
        this.sessionKey = sessionKey == null ? null : sessionKey.trim();
    }

    public Date getAuthorizeTime() {
        return authorizeTime;
    }

    public void setAuthorizeTime(Date authorizeTime) {
        this.authorizeTime = authorizeTime;
    }

    public Date getExpireTime() {
        return expireTime;
    }

    public void setExpireTime(Date expireTime) {
        this.expireTime = expireTime;
    }

    public Date getSysCreateTime() {
        return sysCreateTime;
    }

    public void setSysCreateTime(Date sysCreateTime) {
        this.sysCreateTime = sysCreateTime;
    }

    public Date getSysUpdateTime() {
        return sysUpdateTime;
    }

    public void setSysUpdateTime(Date sysUpdateTime) {
        this.sysUpdateTime = sysUpdateTime;
    }

    public Boolean getSysBlockFlag() {
        return sysBlockFlag;
    }

    public void setSysBlockFlag(Boolean sysBlockFlag) {
        this.sysBlockFlag = sysBlockFlag;
    }
}