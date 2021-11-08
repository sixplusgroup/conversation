package finley.gmair.model.payment;

import java.sql.Timestamp;

public class Trade {

    private String tradeId;
    private String orderId;
    private String tradeDescription;
    private String tradeNonceStr;
    private int tradeTotalFee;
    private String tradeSpbillCreateIp;
    private String tradeType;
    private String tradeOpenId;
    private Timestamp tradeStartTime;
    private Timestamp tradeEndTime;
    private TradeState tradeState;
    private String payClient;// OFFICIALACCOUNT:official account, SHOPMP:mini program

    public Trade () {
        super();
    }

    public String getTradeId() {
        return tradeId;
    }

    public void setTradeId(String tradeId) {
        this.tradeId = tradeId;
    }

    public String getOrderId() {
        return orderId;
    }

    public void setOrderId(String orderId) {
        this.orderId = orderId;
    }

    public String getTradeDescription() {
        return tradeDescription;
    }

    public void setTradeDescription(String tradeDescription) {
        this.tradeDescription = tradeDescription;
    }

    public String getTradeNonceStr() {
        return tradeNonceStr;
    }

    public void setTradeNonceStr(String tradeNonceStr) {
        this.tradeNonceStr = tradeNonceStr;
    }

    public int getTradeTotalFee() {
        return tradeTotalFee;
    }

    public void setTradeTotalFee(int tradeTotalFee) {
        this.tradeTotalFee = tradeTotalFee;
    }

    public String getTradeSpbillCreateIp() {
        return tradeSpbillCreateIp;
    }

    public void setTradeSpbillCreateIp(String tradeSpbillCreateIp) {
        this.tradeSpbillCreateIp = tradeSpbillCreateIp;
    }

    public String getTradeType() {
        return tradeType;
    }

    public void setTradeType(String tradeType) {
        this.tradeType = tradeType;
    }

    public String getTradeOpenId() {
        return tradeOpenId;
    }

    public void setTradeOpenId(String tradeOpenId) {
        this.tradeOpenId = tradeOpenId;
    }

    public Timestamp getTradeStartTime() {
        return tradeStartTime;
    }

    public void setTradeStartTime(Timestamp tradeStartTime) {
        this.tradeStartTime = tradeStartTime;
    }

    public Timestamp getTradeEndTime() {
        return tradeEndTime;
    }

    public void setTradeEndTime(Timestamp tradeEndTime) {
        this.tradeEndTime = tradeEndTime;
    }

    public TradeState getTradeState() {
        return tradeState;
    }

    public void setTradeState(TradeState tradeState) {
        this.tradeState = tradeState;
    }

    public String getPayClient() {
        return payClient;
    }

    public void setPayClient(String payClient) {
        this.payClient = payClient;
    }
}
