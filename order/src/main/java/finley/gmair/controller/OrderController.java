package finley.gmair.controller;

import finley.gmair.model.order.PlatformOrder;
import finley.gmair.service.OrderService;
import finley.gmair.util.RequestUtil;
import finley.gmair.util.ResponseCode;
import finley.gmair.util.ResultData;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.multipart.MultipartHttpServletRequest;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RequestMapping("/order")
@RestController
public class OrderController {
    @Autowired
    private OrderService orderService;

    /**
     * This method is aimed to handle the order spreadsheet and store the records
     *
     * @param request
     * @return
     */
    @RequestMapping(method = RequestMethod.GET, value = "/upload")
    public ResultData upload(MultipartHttpServletRequest request) {
        ResultData result = new ResultData();
        MultipartFile file = RequestUtil.getFile(request, "order-list");
        ResultData response = orderService.process(file);
        if (response.getResponseCode() == ResponseCode.RESPONSE_ERROR) {
            result.setResponseCode(ResponseCode.RESPONSE_ERROR);
            result.setDescription("Fail to process the file");
            return result;
        }
        List<PlatformOrder> list = (List<PlatformOrder>) response.getData();
        new Thread(() -> list.forEach(item -> orderService.createPlatformOrder(item))).start();
        result.setResponseCode(ResponseCode.RESPONSE_OK);
        result.setDescription("Orders are in process, please check it later");
        return result;
    }

    @RequestMapping(method = RequestMethod.GET, value = "/{orderId}/info")
    public ResultData info(@PathVariable("orderId") String orderId) {
        ResultData result = new ResultData();
        Map<String, Object> condition = new HashMap<>();
        condition.put("orderId", orderId);
        ResultData response = orderService.fetchPlatformOrder(condition);
        if (response.getResponseCode() == ResponseCode.RESPONSE_NULL) {
            result.setResponseCode(ResponseCode.RESPONSE_NULL);
            result.setDescription("Please make sure you have the correct order number");
            return result;
        }
        if (response.getResponseCode() == ResponseCode.RESPONSE_ERROR) {
            result.setResponseCode(ResponseCode.RESPONSE_ERROR);
            result.setDescription("Fail to retrieve the order information, please try again later");
        }
        result.setResponseCode(ResponseCode.RESPONSE_OK);
        result.setData(response.getData());
        return result;
    }
}
