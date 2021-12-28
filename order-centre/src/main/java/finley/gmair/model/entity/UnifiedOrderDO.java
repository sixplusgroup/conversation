package finley.gmair.model.entity;

import java.util.Date;

public class UnifiedOrderDO {
    private String orderId;

    private String tradeId;

    private String tid;

    private String oid;

    private Integer status;

    private String numId;

    private String skuId;

    private Integer num;

    private Double price;

    private Double payment;

    private String logisticsCompany;

    private String logisticsId;

    private Date sysCreateTime;

    private Date sysUpdateTime;

    private Boolean sysBlockFlag;

    public UnifiedOrderDO(String orderId, String tradeId, String tid, String oid, Integer status, String numId, String skuId, Integer num, Double price, Double payment, String logisticsCompany, String logisticsId, Date sysCreateTime, Date sysUpdateTime, Boolean sysBlockFlag) {
        this.orderId = orderId;
        this.tradeId = tradeId;
        this.tid = tid;
        this.oid = oid;
        this.status = status;
        this.numId = numId;
        this.skuId = skuId;
        this.num = num;
        this.price = price;
        this.payment = payment;
        this.logisticsCompany = logisticsCompany;
        this.logisticsId = logisticsId;
        this.sysCreateTime = sysCreateTime;
        this.sysUpdateTime = sysUpdateTime;
        this.sysBlockFlag = sysBlockFlag;
    }

    public UnifiedOrderDO() {
        super();
    }

    public String getOrderId() {
        return orderId;
    }

    public void setOrderId(String orderId) {
        this.orderId = orderId == null ? null : orderId.trim();
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

    public String getOid() {
        return oid;
    }

    public void setOid(String oid) {
        this.oid = oid == null ? null : oid.trim();
    }

    public Integer getStatus() {
        return status;
    }

    public void setStatus(Integer status) {
        this.status = status;
    }

    public String getNumId() {
        return numId;
    }

    public void setNumId(String numId) {
        this.numId = numId == null ? null : numId.trim();
    }

    public String getSkuId() {
        return skuId;
    }

    public void setSkuId(String skuId) {
        this.skuId = skuId == null ? null : skuId.trim();
    }

    public Integer getNum() {
        return num;
    }

    public void setNum(Integer num) {
        this.num = num;
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

    public String getLogisticsCompany() {
        return logisticsCompany;
    }

    public void setLogisticsCompany(String logisticsCompany) {
        this.logisticsCompany = logisticsCompany == null ? null : logisticsCompany.trim();
    }

    public String getLogisticsId() {
        return logisticsId;
    }

    public void setLogisticsId(String logisticsId) {
        this.logisticsId = logisticsId == null ? null : logisticsId.trim();
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