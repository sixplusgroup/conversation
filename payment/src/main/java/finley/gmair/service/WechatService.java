package finley.gmair.service;

import finley.gmair.util.ResultData;

public interface WechatService {

    ResultData payCreate(String orderId, String openId, String money, String ipAddress, String body);

    String payNotify(String notifyXml);

    ResultData getTradeByOrderId(String orderId);
}