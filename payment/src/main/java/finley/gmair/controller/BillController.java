package finley.gmair.controller;

import finley.gmair.service.WechatService;
import finley.gmair.util.ResponseCode;
import finley.gmair.util.ResultData;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * @ClassName: PaymentController
 * @Description: TODO
 * @Author fan
 * @Date 2019/7/23 4:25 PM
 */
@RestController
@RequestMapping("/payment/bill")
public class BillController {

    @Autowired
    WechatService wechatService;

    /**
     *
     * @param orderId 订单号
     * @param openid 用户openid
     * @param price 交易金额，单位：分
     * @param body 格式：商家名称-销售商品类目
     * @param ip 终端ip，调用微信支付API的机器IP
     * @return
     */
    @PostMapping("/create")
    public ResultData createTrade(String orderId, String openid, int price, String body, String ip) {
        ResultData result = new ResultData();

        ResultData response = wechatService.payCreate(orderId,openid,price+"", ip, body);
        if (response.getResponseCode() == ResponseCode.RESPONSE_ERROR) {
            result.setResponseCode(ResponseCode.RESPONSE_ERROR);
            result.setDescription(response.getDescription());
        }
        if (response.getResponseCode() == ResponseCode.RESPONSE_OK) {
            result.setResponseCode(ResponseCode.RESPONSE_OK);
            result.setData(response.getData());
        }
        return result;
    }

    /**
     * 微信支付回调方法
     * @param xml
     * @return
     */
    @PostMapping("/notify")
    public String notified(String xml) {
        return wechatService.payNotify(xml);
    }
}
