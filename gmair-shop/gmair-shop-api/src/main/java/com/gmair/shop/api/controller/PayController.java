

package com.gmair.shop.api.controller;

import com.github.binarywang.wxpay.bean.order.WxPayMpOrderResult;
import com.github.binarywang.wxpay.bean.request.WxPayUnifiedOrderRequest;
import com.github.binarywang.wxpay.constant.WxPayConstants;
import com.github.binarywang.wxpay.service.WxPayService;
import com.gmair.shop.api.config.ApiConfig;
import com.gmair.shop.bean.app.param.PayParam;
import com.gmair.shop.bean.pay.PayInfoDto;
import com.gmair.shop.common.util.Arith;
import com.gmair.shop.common.util.IPHelper;
import com.gmair.shop.security.entity.GmairUser;
import com.gmair.shop.security.util.SecurityUtils;
import com.gmair.shop.service.PayService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.AllArgsConstructor;
import lombok.SneakyThrows;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/p/order")
@Api(tags = "订单接口")
@AllArgsConstructor
public class PayController {

    private final PayService payService;

    private final ApiConfig apiConfig;

    private final WxPayService wxMiniPayService;

    /**
     * 支付接口
     */
    @PostMapping("/pay")
    @ApiOperation(value = "根据订单号进行支付", notes = "根据订单号进行支付")
    @SneakyThrows
    public ResponseEntity<WxPayMpOrderResult> pay(@RequestBody PayParam payParam) {
        //ResponseEntity<Map<String,String>> ResponseEntity<WxPayMpOrderResult>
        GmairUser user = SecurityUtils.getUser();
        String userId = user.getUserId();
        String openId = user.getBizUserId();


        PayInfoDto payInfo = payService.pay(userId, payParam);

        WxPayUnifiedOrderRequest orderRequest = new WxPayUnifiedOrderRequest();
        orderRequest.setBody(payInfo.getBody());
        orderRequest.setOutTradeNo(payInfo.getPayNo());
        orderRequest.setTotalFee((int) Arith.mul(payInfo.getPayAmount(), 100));
        orderRequest.setSpbillCreateIp(IPHelper.getIpAddr());
        orderRequest.setNotifyUrl(apiConfig.getDomainName() + "/notice/pay/order");
        orderRequest.setTradeType(WxPayConstants.TradeType.JSAPI);
        orderRequest.setOpenid(openId);
        ResponseEntity<WxPayMpOrderResult> result = ResponseEntity.ok(wxMiniPayService.createOrder(orderRequest));
        return result;


        // try one
//        return ResponseEntity.ok(new WxPayMpOrderResult("wx8888888888888888","1412000000","5K8264ILTKCH16CQ2502SI8ZNMTM67VS","Sign=WXPay","HMAC-SHA256","C380BEC2BFD727A4B6845133519F3AD6"));
//        return ResponseEntity.ok((WxPayMpOrderResult)new Object());

        // try two
//        {
//                "appId": "wx7c4e32274bf32ba4",
//                "nonceStr": "5K8264ILTKCH16CQ2502SI8ZNMTM67VS",
//                "packageValue": "Sign=WXPay",
//                "paySign": "C380BEC2BFD727A4B6845133519F3AD6",
//                "signType": "HMAC-SHA256",
//                "timeStamp": "1412000000"
//        }
//        Map<String,String> map = new HashMap<>();
//        map.put("appId", "wx7c4e32274bf32ba4");
//        map.put("nonceStr", "5K8264ILTKCH16CQ2502SI8ZNMTM67VS");
//        map.put("packageValue", "Sign=WXPay");
//        map.put("paySign", "C380BEC2BFD727A4B6845133519F3AD6");
//        map.put("signType","HMAC-SHA256");
//        map.put("timeStamp", "1412000000");
//        return ResponseEntity.ok(map);
    }

    /**
     * 普通支付接口
     */
    @PostMapping("/normalPay")
    @ApiOperation(value = "根据订单号进行支付", notes = "根据订单号进行支付")
    @SneakyThrows
    public ResponseEntity<Boolean> normalPay(@RequestBody PayParam payParam) {

        GmairUser user = SecurityUtils.getUser();
        String userId = user.getUserId();
        PayInfoDto pay = payService.pay(userId, payParam);

        // 根据内部订单号更新order settlement
        payService.paySuccess(pay.getPayNo(), "");

        return ResponseEntity.ok(true);
    }
}
