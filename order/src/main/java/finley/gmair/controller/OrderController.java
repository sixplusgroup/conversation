package finley.gmair.controller;

import finley.gmair.model.order.PlatformOrder;
import finley.gmair.service.OrderService;
import finley.gmair.util.RequestUtil;
import finley.gmair.util.ResponseCode;
import finley.gmair.util.ResultData;
import org.omg.PortableServer.LIFESPAN_POLICY_ID;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.multipart.MultipartHttpServletRequest;

import java.util.List;

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

        return result;
    }

    @RequestMapping(method = RequestMethod.GET, value = "/{orderId}/info")
    public ResultData info(@PathVariable("orderId") String orderId) {
        ResultData result = new ResultData();

        return result;
    }
}
