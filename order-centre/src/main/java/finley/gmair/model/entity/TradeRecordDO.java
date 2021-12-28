package finley.gmair.model.entity;

import java.util.Date;

public class TradeRecordDO {
    private String recordId;

    private Integer platform;

    private String shopId;

    private String tid;

    private String orderId;

    private String recordMessage;

    private String userName;

    private Date sysCreateTime;

    private Date sysUpdateTime;

    private Boolean sysBlockFlag;

    private String tradeData;

    public TradeRecordDO(String recordId, Integer platform, String shopId, String tid, String orderId, String recordMessage, String userName, Date sysCreateTime, Date sysUpdateTime, Boolean sysBlockFlag) {
        this.recordId = recordId;
        this.platform = platform;
        this.shopId = shopId;
        this.tid = tid;
        this.orderId = orderId;
        this.recordMessage = recordMessage;
        this.userName = userName;
        this.sysCreateTime = sysCreateTime;
        this.sysUpdateTime = sysUpdateTime;
        this.sysBlockFlag = sysBlockFlag;
    }

    public TradeRecordDO(String recordId, Integer platform, String shopId, String tid, String orderId, String recordMessage, String userName, Date sysCreateTime, Date sysUpdateTime, Boolean sysBlockFlag, String tradeData) {
        this.recordId = recordId;
        this.platform = platform;
        this.shopId = shopId;
        this.tid = tid;
        this.orderId = orderId;
        this.recordMessage = recordMessage;
        this.userName = userName;
        this.sysCreateTime = sysCreateTime;
        this.sysUpdateTime = sysUpdateTime;
        this.sysBlockFlag = sysBlockFlag;
        this.tradeData = tradeData;
    }

    public TradeRecordDO() {
        super();
    }

    public String getRecordId() {
        return recordId;
    }

    public void setRecordId(String recordId) {
        this.recordId = recordId == null ? null : recordId.trim();
    }

    public Integer getPlatform() {
        return platform;
    }

    public void setPlatform(Integer platform) {
        this.platform = platform;
    }

    public String getShopId() {
        return shopId;
    }

    public void setShopId(String shopId) {
        this.shopId = shopId == null ? null : shopId.trim();
    }

    public String getTid() {
        return tid;
    }

    public void setTid(String tid) {
        this.tid = tid == null ? null : tid.trim();
    }

    public String getOrderId() {
        return orderId;
    }

    public void setOrderId(String orderId) {
        this.orderId = orderId == null ? null : orderId.trim();
    }

    public String getRecordMessage() {
        return recordMessage;
    }

    public void setRecordMessage(String recordMessage) {
        this.recordMessage = recordMessage == null ? null : recordMessage.trim();
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName == null ? null : userName.trim();
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

    public String getTradeData() {
        return tradeData;
    }

    public void setTradeData(String tradeData) {
        this.tradeData = tradeData == null ? null : tradeData.trim();
    }
}