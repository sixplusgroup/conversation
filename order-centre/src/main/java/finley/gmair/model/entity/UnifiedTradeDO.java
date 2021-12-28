package finley.gmair.model.entity;

import java.util.Date;

public class UnifiedTradeDO {
    private String tradeId;

    private String tid;

    private Integer tradePlatform;

    private String shopId;

    private Integer status;

    private Date createTime;

    private Date updateTime;

    private Date payTime;

    private Date endTime;

    private String consigneeName;

    private String consigneePhone;

    private String consigneeProvince;

    private String consigneeCity;

    private String consigneeDistrict;

    private String consigneeAddress;

    private Double price;

    private Double payment;

    private Double postFee;

    private String buyerMessage;

    private String sellerMemo;

    private Boolean isFuzzy;

    private Integer crmPushStatus;

    private Integer driftPushStatus;

    private Date sysCreateTime;

    private Date sysUpdateTime;

    private Boolean sysBlockFlag;

    public UnifiedTradeDO(String tradeId, String tid, Integer tradePlatform, String shopId, Integer status, Date createTime, Date updateTime, Date payTime, Date endTime, String consigneeName, String consigneePhone, String consigneeProvince, String consigneeCity, String consigneeDistrict, String consigneeAddress, Double price, Double payment, Double postFee, String buyerMessage, String sellerMemo, Boolean isFuzzy, Integer crmPushStatus, Integer driftPushStatus, Date sysCreateTime, Date sysUpdateTime, Boolean sysBlockFlag) {
        this.tradeId = tradeId;
        this.tid = tid;
        this.tradePlatform = tradePlatform;
        this.shopId = shopId;
        this.status = status;
        this.createTime = createTime;
        this.updateTime = updateTime;
        this.payTime = payTime;
        this.endTime = endTime;
        this.consigneeName = consigneeName;
        this.consigneePhone = consigneePhone;
        this.consigneeProvince = consigneeProvince;
        this.consigneeCity = consigneeCity;
        this.consigneeDistrict = consigneeDistrict;
        this.consigneeAddress = consigneeAddress;
        this.price = price;
        this.payment = payment;
        this.postFee = postFee;
        this.buyerMessage = buyerMessage;
        this.sellerMemo = sellerMemo;
        this.isFuzzy = isFuzzy;
        this.crmPushStatus = crmPushStatus;
        this.driftPushStatus = driftPushStatus;
        this.sysCreateTime = sysCreateTime;
        this.sysUpdateTime = sysUpdateTime;
        this.sysBlockFlag = sysBlockFlag;
    }

    public UnifiedTradeDO() {
        super();
    }

    public String getTradeId() {
        return tradeId;
    }

    public void setTradeId(String tradeId) {
        this.tradeId = tradeId == null ? null : tradeId.trim();
    }

    public String getTid() {
        return tid;
    }

    public void setTid(String tid) {
        this.tid = tid == null ? null : tid.trim();
    }

    public Integer getTradePlatform() {
        return tradePlatform;
    }

    public void setTradePlatform(Integer tradePlatform) {
        this.tradePlatform = tradePlatform;
    }

    public String getShopId() {
        return shopId;
    }

    public void setShopId(String shopId) {
        this.shopId = shopId == null ? null : shopId.trim();
    }

    public Integer getStatus() {
        return status;
    }

    public void setStatus(Integer status) {
        this.status = status;
    }

    public Date getCreateTime() {
        return createTime;
    }

    public void setCreateTime(Date createTime) {
        this.createTime = createTime;
    }

    public Date getUpdateTime() {
        return updateTime;
    }

    public void setUpdateTime(Date updateTime) {
        this.updateTime = updateTime;
    }

    public Date getPayTime() {
        return payTime;
    }

    public void setPayTime(Date payTime) {
        this.payTime = payTime;
    }

    public Date getEndTime() {
        return endTime;
    }

    public void setEndTime(Date endTime) {
        this.endTime = endTime;
    }

    public String getConsigneeName() {
        return consigneeName;
    }

    public void setConsigneeName(String consigneeName) {
        this.consigneeName = consigneeName == null ? null : consigneeName.trim();
    }

    public String getConsigneePhone() {
        return consigneePhone;
    }

    public void setConsigneePhone(String consigneePhone) {
        this.consigneePhone = consigneePhone == null ? null : consigneePhone.trim();
    }

    public String getConsigneeProvince() {
        return consigneeProvince;
    }

    public void setConsigneeProvince(String consigneeProvince) {
        this.consigneeProvince = consigneeProvince == null ? null : consigneeProvince.trim();
    }

    public String getConsigneeCity() {
        return consigneeCity;
    }

    public void setConsigneeCity(String consigneeCity) {
        this.consigneeCity = consigneeCity == null ? null : consigneeCity.trim();
    }

    public String getConsigneeDistrict() {
        return consigneeDistrict;
    }

    public void setConsigneeDistrict(String consigneeDistrict) {
        this.consigneeDistrict = consigneeDistrict == null ? null : consigneeDistrict.trim();
    }

    public String getConsigneeAddress() {
        return consigneeAddress;
    }

    public void setConsigneeAddress(String consigneeAddress) {
        this.consigneeAddress = consigneeAddress == null ? null : consigneeAddress.trim();
    }

    public Double getPrice() {
        return price;
    }

    public void setPrice(Double price) {
        this.price = price;
    }

    public Double getPayment() {
        return payment;
    }

    public void setPayment(Double payment) {
        this.payment = payment;
    }

    public Double getPostFee() {
        return postFee;
    }

    public void setPostFee(Double postFee) {
        this.postFee = postFee;
    }

    public String getBuyerMessage() {
        return buyerMessage;
    }

    public void setBuyerMessage(String buyerMessage) {
        this.buyerMessage = buyerMessage == null ? null : buyerMessage.trim();
    }

    public String getSellerMemo() {
        return sellerMemo;
    }

    public void setSellerMemo(String sellerMemo) {
        this.sellerMemo = sellerMemo == null ? null : sellerMemo.trim();
    }

    public Boolean getIsFuzzy() {
        return isFuzzy;
    }

    public void setIsFuzzy(Boolean isFuzzy) {
        this.isFuzzy = isFuzzy;
    }

    public Integer getCrmPushStatus() {
        return crmPushStatus;
    }

    public void setCrmPushStatus(Integer crmPushStatus) {
        this.crmPushStatus = crmPushStatus;
    }

    public Integer getDriftPushStatus() {
        return driftPushStatus;
    }

    public void setDriftPushStatus(Integer driftPushStatus) {
        this.driftPushStatus = driftPushStatus;
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