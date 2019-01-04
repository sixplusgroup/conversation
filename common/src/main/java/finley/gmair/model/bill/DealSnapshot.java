package finley.gmair.model.bill;

import finley.gmair.model.Entity;

public class DealSnapshot extends Entity {

    private String snapshotId;

    private String appId;

    private String billId;

    private String mchId;

    private String deviceInfo;

    private String isSubscribe;

    private String channelId;

    private String bankType;

    private double orderPrice;

    private double actualPrice;

    private String feeType;

    private String timeEnd;

    private String accountId;

    private String transactionId;

    public DealSnapshot(double actualPrice, String billId, String channelId, String appId, String accountId,
                        String mchId, String deviceInfo, String isSubscribe, String bankType, double orderPrice,
                        String feeType, String transactionId, String timeEnd) {
        super();
        this.actualPrice = actualPrice;
        this.billId = billId;
        this.channelId = channelId;
        this.appId = appId;
        this.accountId = accountId;
        this.timeEnd = timeEnd;
        this.feeType = feeType;
        this.transactionId = transactionId;
        this.bankType = bankType;
        this.deviceInfo = deviceInfo;
        this.orderPrice = orderPrice;
        this.isSubscribe = isSubscribe;
        this.mchId = mchId;

    }


    public String getBillId() {
        return billId;
    }

    public void setBillId(String billId) {
        this.billId = billId;
    }

    public String getSnapshotId() {
        return snapshotId;
    }

    public void setSnapshotId(String snapshotId) {
        this.snapshotId = snapshotId;
    }

    public String getAppId() {
        return appId;
    }

    public void setAppId(String appId) {
        this.appId = appId;
    }

    public String getMchId() {
        return mchId;
    }

    public void setMchId(String mchId) {
        this.mchId = mchId;
    }

    public String getDeviceInfo() {
        return deviceInfo;
    }

    public void setDeviceInfo(String deviceInfo) {
        this.deviceInfo = deviceInfo;
    }

    public String getIsSubscribe() {
        return isSubscribe;
    }

    public void setIsSubscribe(String isSubscribe) {
        this.isSubscribe = isSubscribe;
    }

    public String getBankType() {
        return bankType;
    }

    public void setBankType(String bankType) {
        this.bankType = bankType;
    }

    public String getFeeType() {
        return feeType;
    }

    public void setFeeType(String feeType) {
        this.feeType = feeType;
    }

    public String getTimeEnd() {
        return timeEnd;
    }

    public void setTimeEnd(String timeEnd) {
        this.timeEnd = timeEnd;
    }

    public String getAccountId() {
        return accountId;
    }

    public void setAccountId(String accountId) {
        this.accountId = accountId;
    }

    public String getChannelId() {
        return channelId;
    }

    public void setChannelId(String channelId) {
        this.channelId = channelId;
    }

    public void setOrderPrice(double orderPrice) {
        this.orderPrice = orderPrice;
    }

    public void setActualPrice(double actualPrice) {
        this.actualPrice = actualPrice;
    }

    public double getOrderPrice() {
        return orderPrice;
    }

    public double getActualPrice() {
        return actualPrice;
    }

    public String getTransactionId() {
        return transactionId;
    }

    public void setTransactionId(String transactionId) {
        this.transactionId = transactionId;
    }


}
