package gmair.finley.controller;


import finley.gmair.model.order.OrderChannel;
import finley.gmair.util.ResponseCode;
import finley.gmair.util.ResultData;
import gmair.finley.service.OrderChannelService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import java.util.HashMap;
import java.util.Map;

@RestController
public class OrderChannelController {

    @Autowired
    private OrderChannelService orderChannelService;

    private Logger logger = LoggerFactory.getLogger(OrderChannelController.class);

    @RequestMapping(method = RequestMethod.GET, value = "/orderChannel/list")
    public ResultData OrderChannel() {
        ResultData result = new ResultData();
        Map<String, Object> condition = new HashMap<>();
        condition.put("blockFlag", false);
        ResultData response = orderChannelService.fetchOrderChannel(condition);
        if (response.getResponseCode() == ResponseCode.RESPONSE_NULL) {
            result.setResponseCode(ResponseCode.RESPONSE_NULL);
        } else if (response.getResponseCode() == ResponseCode.RESPONSE_ERROR) {
            result.setResponseCode(ResponseCode.RESPONSE_ERROR);
            result.setDescription("服务器异常，请稍后重试");
        } else {
            result.setData(response.getData());
        }
        return result;
    }

    @RequestMapping(method = RequestMethod.POST, value = "/orderChannel/create")
    public ResultData createOrderChannel(OrderChannel orderChannel) {
        ResultData result = new ResultData();
        ResultData response = orderChannelService.create(orderChannel);
        result.setResponseCode(response.getResponseCode());
        if (response.getResponseCode() == ResponseCode.RESPONSE_OK) {
            result.setData(response.getData());
        } else if (response.getResponseCode() == ResponseCode.RESPONSE_ERROR) {
            result.setDescription("服务器忙，请稍后再试!");
        }
        return result;
    }

    @RequestMapping(method = RequestMethod.POST, value = "/orderChannel/update")
    public ResultData updateOrderChannel(OrderChannel orderChannel) {
        ResultData result = new ResultData();
        ResultData response = orderChannelService.modifyOrderChannel(orderChannel);
        result.setResponseCode(response.getResponseCode());
        if (response.getResponseCode() == ResponseCode.RESPONSE_OK) {
            result.setData(response.getData());
        } else if (response.getResponseCode() == ResponseCode.RESPONSE_ERROR) {
            result.setDescription("服务器忙，请稍后再试!");
        }
        return result;
    }

    @RequestMapping(method = RequestMethod.POST, value = "/orderChannel/delete/{channelId}")
    public ResultData deleteOrderChannel(@PathVariable String channelId) {

        ResultData result = orderChannelService.deleteOrderChannel(channelId);
        logger.info("delete orderChannel using channelId: " + channelId);
        return result;
    }
}
