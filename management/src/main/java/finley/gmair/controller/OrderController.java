package finley.gmair.controller;

import finley.gmair.service.OrderFormService;
import finley.gmair.service.OrderService;
import finley.gmair.util.ResultData;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartHttpServletRequest;

@RestController
@RequestMapping("/management/order")
public class OrderController {
    @Autowired
    private OrderFormService orderFormService;

    @Autowired
    private OrderService orderService;

    @RequestMapping(method = RequestMethod.POST, value = "/upload")
    public ResultData upload(MultipartHttpServletRequest request) {
        return orderFormService.upload(request.getFile("order_list"));
    }

    @GetMapping("/channel/list")
    public ResultData channelList() {
        return orderService.channelList();
    }

    @GetMapping("/list")
    public ResultData orderList() {
        return orderService.orderList();
    }

    @PostMapping("/create")
    public ResultData orderCreate(String order) {
        return orderService.orderCreate(order);
    }

    @GetMapping("/{orderId}/info")
    public ResultData orderInfo(@PathVariable("orderId") String orderId) {
        return orderService.orderInfo(orderId);
    }

    @GetMapping("/commodity/list")
    public ResultData commodityList() {
        return orderService.commodityList();
    }

    @PostMapping("/deliver")
    public ResultData orderDeliver(String orderId, String company, String expressNo) {
        return orderService.orderDeliver(orderId, company, expressNo);
    }
}
