package gmair.finley.controller;

import finley.gmair.util.ResultData;
import gmair.finley.service.OrderService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartHttpServletRequest;

@RequestMapping("/order")
@RestController
public class OrderController {
    @Autowired
    private OrderService orderService;


    @RequestMapping(method = RequestMethod.GET, value = "/upload")
    public ResultData upload(MultipartHttpServletRequest request) {
        ResultData result = new ResultData();

        return result;
    }

    @RequestMapping(method = RequestMethod.GET, value = "/{orderId}/info")
    public ResultData info(@PathVariable("orderId") String orderId) {
        ResultData result = new ResultData();

        return result;
    }
}
