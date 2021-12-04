package finley.gmair.service;

import finley.gmair.util.ResultData;

public interface WechatService {

    ResultData payCreate(String orderId, String openId, String money, String ipAddress, String body,String payClient);

    ResultData payAllowExist(String orderId, String openId, String money, String ipAddress, String body,String payClient);

    String payNotify(String notifyXml);

    ResultData getTradeByOrderId(String orderId,String payClient);

    ResultData getCreateResult(String orderId,String payClient);

    ResultData checkTradePayed(String payClient);
}