package gmair.finley.controller;


import finley.gmair.model.order.OrderDiversion;
import finley.gmair.util.ResponseCode;
import finley.gmair.util.ResultData;
import gmair.finley.service.OrderDiversionService;
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
@RequestMapping("/orderDiversion")
public class OrderDiversionController {

    private Logger logger = LoggerFactory.getLogger(OrderDiversionController.class);

    @Autowired
    private OrderDiversionService orderDiversionService;

    @RequestMapping(method = RequestMethod.GET, value = "/list")
    public ResultData OrderDiversionList() {
        ResultData result = new ResultData();
        Map<String, Object> condition = new HashMap<>();
        condition.put("blockFlag", false);
        ResultData response = orderDiversionService.fetch(condition);
        result.setResponseCode(response.getResponseCode());
        if (response.getResponseCode() == ResponseCode.RESPONSE_OK) {
            result.setData(response.getData());
        } else {
            result.setDescription(response.getDescription());
        }
        return result;
    }

    @RequestMapping(method = RequestMethod.POST, value = "/create")
    public ResultData CreateOrderDiversion(OrderDiversion orderDiversion) {
        ResultData result = new ResultData();
        ResultData response = orderDiversionService.create(orderDiversion);
        if (response.getResponseCode() == ResponseCode.RESPONSE_NULL) {
            result.setResponseCode(response.getResponseCode());
        } else if (response.getResponseCode() == ResponseCode.RESPONSE_ERROR) {
            result.setResponseCode(ResponseCode.RESPONSE_ERROR);
            result.setDescription("服务器忙，请稍后再试!");
        } else {
            result.setData(response.getData());
        }
        return result;
    }

    @RequestMapping(method = RequestMethod.POST, value = "/update")
    public ResultData UpdateOrderDiversion(OrderDiversion orderDiversion) {
        ResultData result = new ResultData();
        ResultData response = orderDiversionService.modify(orderDiversion);
        result.setResponseCode(response.getResponseCode());
        if (response.getResponseCode() == ResponseCode.RESPONSE_OK) {
            result.setData(response.getData());
        } else if (response.getResponseCode() == ResponseCode.RESPONSE_ERROR) {
            result.setDescription("服务器忙，请稍后再试!");
        }
        return result;
    }

    @RequestMapping(method = RequestMethod.POST, value = "/delete/{diversionId}")
    public ResultData DeleteOrderDiversion(@PathVariable String diversionId) {
        ResultData result = orderDiversionService.delete(diversionId);
        logger.info("delete orderDiversion using diversionId: " + diversionId);
        return result;
    }

}
