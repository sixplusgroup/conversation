package finley.gmair.controller;

import com.alibaba.fastjson.JSONObject;
import finley.gmair.model.order.PlatformOrder;
import finley.gmair.service.ExpressService;
import finley.gmair.service.InstallService;
import finley.gmair.service.OrderService;
import finley.gmair.util.RequestUtil;
import finley.gmair.util.ResponseCode;
import finley.gmair.util.ResultData;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
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

    @Autowired
    private ExpressService expressService;

    @Autowired
    private InstallService installService;

    /**
     * This method is aimed to handle the order spreadsheet and store the records
     *
     * @param request
     * @return
     */
    @CrossOrigin
    @RequestMapping(method = RequestMethod.POST, value = "/upload")
    public ResultData upload(MultipartHttpServletRequest request) {
        ResultData result = new ResultData();
        MultipartFile file = RequestUtil.getFile(request, "order_list");
        ResultData response = orderService.process(file);
        if (response.getResponseCode() == ResponseCode.RESPONSE_ERROR) {
            result.setResponseCode(ResponseCode.RESPONSE_ERROR);
            result.setDescription("Fail to process the file");
            return result;
        }
        result.setResponseCode(ResponseCode.RESPONSE_OK);
        result.setDescription("Orders are in process, please check it later");
        return result;
    }

    @CrossOrigin
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

    @CrossOrigin
    @RequestMapping(method = RequestMethod.GET, value = "/list")
    public ResultData list() {
        ResultData result = new ResultData();
        Map<String, Object> condition = new HashMap<>();
        condition.put("blockFlag", false);
        ResultData response = orderService.fetchPlatformOrder(condition);
        if (response.getResponseCode() == ResponseCode.RESPONSE_NULL) {
            result.setResponseCode(ResponseCode.RESPONSE_NULL);
            result.setDescription("no more order list");
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

    @CrossOrigin
    @RequestMapping(method = RequestMethod.GET, value = "/qrcode/{qrcode}")
    public ResultData orderByQrcode(@PathVariable String qrcode) {
        ResultData result = new ResultData();
        ResultData response = expressService.getOrderByQrcode(qrcode);
        if (response.getResponseCode() == ResponseCode.RESPONSE_ERROR) {
            result.setResponseCode(ResponseCode.RESPONSE_ERROR);
            result.setDescription("快递查询忙，请稍后再试！");
        } else if (response.getResponseCode() == ResponseCode.RESPONSE_NULL) {
            result.setResponseCode(ResponseCode.RESPONSE_NULL);
            result.setDescription("没有二维码关联的机器");
        }else {
            JSONObject jsonObject = JSONObject.parseObject(response.getData().toString());
            String orderId = jsonObject.getString("orderId");
            Map<String, Object> condition = new HashMap<>();
            condition.put("orderId", orderId);
            condition.put("blockFlag", false);
            response = orderService.fetchPlatformOrder(condition);
            if (response.getResponseCode() == ResponseCode.RESPONSE_NULL) {
                result.setResponseCode(ResponseCode.RESPONSE_NULL);
                result.setDescription("没有二维码相关联的订单");
                return result;
            }
            if (response.getResponseCode() == ResponseCode.RESPONSE_ERROR) {
                result.setResponseCode(ResponseCode.RESPONSE_ERROR);
                result.setDescription("订单查询忙，请稍后再试");
            }
            result.setResponseCode(ResponseCode.RESPONSE_OK);
            result.setData(((List<PlatformOrder>)response.getData()).get(0));
            return result;
        }
        return result;
    }

    @CrossOrigin
    @RequestMapping(method = RequestMethod.POST, value = "/deliver")
    public ResultData orderDeliver(@RequestParam("orderId") String orderId,
                                   @RequestParam(value = "qrcode", required = false) String qrcode
                                   ){
        ResultData result = new ResultData();
        if (null == orderId) {
            result.setResponseCode(ResponseCode.RESPONSE_ERROR);
            result.setDescription("parameter orderId is required.");
            return result;
        }
        Map<String, Object> condition = new HashMap<>();
        condition.put("orderId", orderId);
        condition.put("blockFlag", false);
        ResultData response = orderService.fetchPlatformOrder(condition);
        if (response.getResponseCode() == ResponseCode.RESPONSE_ERROR) {
            result.setResponseCode(ResponseCode.RESPONSE_ERROR);
            result.setDescription("订单服务器忙，请稍后再试！");
            return result;
        } else if (response.getResponseCode() == ResponseCode.RESPONSE_NULL) {
            result.setResponseCode(ResponseCode.RESPONSE_NULL);
            result.setDescription("该订单不存在，请确认订单号");
            return result;
        }
        PlatformOrder order = ((List<PlatformOrder>) response.getData()).get(0);

        // create install order
        if (null != qrcode) {
            response = installService.create(qrcode, order.getConsignee(), order.getPhone(), order.getAddress());

            if (response.getResponseCode() == ResponseCode.RESPONSE_ERROR) {
                result.setResponseCode(ResponseCode.RESPONSE_ERROR);
                result.setDescription("安装服务器忙，请稍后再试！");
                return result;
            } else if (response.getResponseCode() == ResponseCode.RESPONSE_NULL) {
                result.setResponseCode(ResponseCode.RESPONSE_NULL);
                result.setDescription("该订单不存在，请确认订单号");
                return result;
            } else {
                result.setData(response.getData());
            }
        }

        return result;
    }
}
