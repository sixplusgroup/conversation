package finley.gmair.controller;

import finley.gmair.model.drift.Activity;
import finley.gmair.model.drift.DriftOrder;
import finley.gmair.model.drift.DriftOrderStatus;
import finley.gmair.service.ActivityService;
import finley.gmair.service.OrderService;
import finley.gmair.service.PaymentService;
import finley.gmair.util.IPUtil;
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

import javax.servlet.http.HttpServletRequest;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

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

    @Autowired
    private OrderService orderService;

    @Autowired
    private ActivityService activityService;

    @GetMapping("/{orderId}/info")
    public ResultData bill(@PathVariable("orderId") String orderId, HttpServletRequest request) {
        ResultData result = new ResultData();
        if (StringUtil.isEmpty(orderId)) {
            result.setResponseCode(ResponseCode.RESPONSE_ERROR);
            result.setDescription("请提供所要查询付款金额的订单信息");
            return result;
        }
        Map<String, Object> condition = new HashMap<>();
        condition.put("orderId", orderId);
        condition.put("blockFlag", false);
        condition.put("status", DriftOrderStatus.APPLIED.getValue());
        ResultData response = orderService.fetchDriftOrder(condition);
        if (response.getResponseCode() != ResponseCode.RESPONSE_OK) {
            result.setResponseCode(ResponseCode.RESPONSE_ERROR);
            result.setDescription("订单信息有误，请确认后重试");
            return result;
        }
        DriftOrder order = ((List<DriftOrder>) response.getData()).get(0);
        String ip = IPUtil.getIP(request);
        condition.clear();
        condition.put("blockFlag", false);
        condition.put("activityId", order.getActivityId());
        response = activityService.fetchActivity(condition);
        if (response.getResponseCode() != ResponseCode.RESPONSE_OK) {
            result.setResponseCode(ResponseCode.RESPONSE_ERROR);
            result.setDescription("活动信息有误，请确认后重试");
            return result;
        }
        String activityName = ((List<Activity>) response.getData()).get(0).getActivityName();
        result = paymentService.getTrade(orderId);
        if (result.getResponseCode() == ResponseCode.RESPONSE_OK) {
            return result;
        }
        result = paymentService.createPay(orderId, order.getConsumerId(), (int) (order.getRealPay() * 100), activityName, ip);
        if (result.getResponseCode() == ResponseCode.RESPONSE_OK) {
            return paymentService.getTrade(orderId);
        }
        result.setResponseCode(ResponseCode.RESPONSE_ERROR);
        result.setDescription("交易失败，请稍后重试");
        return result;
    }
}
