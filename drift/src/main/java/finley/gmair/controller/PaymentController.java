package finley.gmair.controller;

import finley.gmair.service.PaymentService;
import finley.gmair.util.ResponseCode;
import finley.gmair.util.ResultData;
import finley.gmair.util.StringUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * @ClassName: PaymentController
 * @Description: This class is responsible for drift order related payment information
 * @Author fan
 * @Date 2019/7/29 9:19 AM
 */
@RestController
@RequestMapping("/drift/order/payment")
public class PaymentController {
    private Logger logger = LoggerFactory.getLogger(PaymentController.class);

    @Autowired
    private PaymentService paymentService;

    @GetMapping("/{orderId}/info")
    public ResultData bill(@PathVariable("orderId") String orderId) {
        ResultData result = new ResultData();
        if (StringUtil.isEmpty(orderId)) {
            result.setResponseCode(ResponseCode.RESPONSE_ERROR);
            result.setDescription("请提供所要查询付款金额的订单信息");
            return result;
        }
        result = paymentService.getTrade(orderId);
        return result;
    }
}
