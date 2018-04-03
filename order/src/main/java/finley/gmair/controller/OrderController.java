package finley.gmair.controller;

import finley.gmair.service.OrderService;
import finley.gmair.util.RequestUtil;
import finley.gmair.util.ResultData;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.multipart.MultipartHttpServletRequest;

@RequestMapping("/order")
@RestController
public class OrderController {
    @Autowired
    private OrderService orderService;

    @RequestMapping(method = RequestMethod.GET, value = "/upload")
    public ResultData upload(MultipartHttpServletRequest request) {
        ResultData result = new ResultData();
        MultipartFile file = RequestUtil.getFile(request, "order-list");

        return result;
    }

    @RequestMapping(method = RequestMethod.GET, value = "/{orderId}/info")
    public ResultData info(@PathVariable("orderId") String orderId) {
        ResultData result = new ResultData();

        return result;
    }
}
