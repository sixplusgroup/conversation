

package com.gmair.shop.api.controller;

import com.github.binarywang.wxpay.bean.notify.WxPayOrderNotifyResult;
import com.github.binarywang.wxpay.exception.WxPayException;
import com.github.binarywang.wxpay.service.WxPayService;
import com.gmair.shop.service.PayService;
import lombok.AllArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import springfox.documentation.annotations.ApiIgnore;

@ApiIgnore
@RestController
@RequestMapping("/shop/consumer/notice/pay")
@AllArgsConstructor
public class PayNoticeController {

    /**
     * 小程序支付
     */
    private final WxPayService wxMiniPayService;

    private final PayService payService;

    // 更新order
    // 更新settlement == payment.trade
//    @RequestMapping("/order")
//    public ResponseEntity<Void> submit(@RequestBody String xmlData) throws WxPayException {
//        WxPayOrderNotifyResult parseOrderNotifyResult = wxMiniPayService.parseOrderNotifyResult(xmlData);
//
//        String payNo = parseOrderNotifyResult.getOutTradeNo();
//        String bizPayNo = parseOrderNotifyResult.getTransactionId();
//
//        // 根据内部订单号更新order settlement
//        payService.paySuccess(payNo, bizPayNo);
//
//
//        return ResponseEntity.ok().build();
//    }
}