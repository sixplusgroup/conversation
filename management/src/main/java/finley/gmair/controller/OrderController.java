package finley.gmair.controller;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import finley.gmair.form.order.OrderDeliverForm;
import finley.gmair.model.order.PlatformOrder;
import finley.gmair.service.ExpressService;
import finley.gmair.service.InstallService;
import finley.gmair.service.OrderFormService;
import finley.gmair.service.OrderService;
import finley.gmair.util.ResponseCode;
import finley.gmair.util.ResultData;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartHttpServletRequest;

import java.util.LinkedHashMap;
import java.util.List;

@CrossOrigin
@RestController
@RequestMapping("/management/order")
public class OrderController {
    @Autowired
    private OrderFormService orderFormService;

    @Autowired
    private OrderService orderService;

    @Autowired
    private ExpressService expressService;

    @Autowired
    private InstallService installService;

    @RequestMapping(method = RequestMethod.POST, value = "/upload")
    public ResultData upload(MultipartHttpServletRequest request) {
        return orderFormService.upload(request.getFile("order_list"));
    }

    @GetMapping("/channel/list")
    public ResultData channelList() {
        return orderService.channelList();
    }

    @GetMapping("/list")
    public ResultData orderList(String startTime, String endTime, String cityName, String status) {
        return orderService.orderList(startTime,endTime,cityName,status);
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
    public ResultData orderDeliver(OrderDeliverForm orderDeliverForm) {
        ResultData result = new ResultData();
        try {
            ResultData response = orderService.orderDeliver(orderDeliverForm.getOrderId());

            response = orderService.orderInfo(orderDeliverForm.getOrderId());

            if (response.getResponseCode() != ResponseCode.RESPONSE_OK) {
                result.setResponseCode(response.getResponseCode());
                result.setDescription(response.getDescription());
                return result;
            }

            JSONObject jsonObject = new JSONObject((LinkedHashMap) response.getData());
            String consignee = jsonObject.getString("consignee");
            String phone = jsonObject.getString("phone");
            String address = jsonObject.getString("address");

            response = expressService.createExpressForm(orderDeliverForm.getOrderId(),
                    orderDeliverForm.getCompany(), orderDeliverForm.getExpressNo(), orderDeliverForm.getQrcode());
            String[] qrcodeList = orderDeliverForm.getQrcode().split(",");
            if (response.getResponseCode() != ResponseCode.RESPONSE_OK) {
                response.setResponseCode(ResponseCode.RESPONSE_ERROR);
            }
            for (String qrcode : qrcodeList) {
                response = installService.createInstallationAssign(qrcode, consignee, phone, address);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        return result;
    }

    @PostMapping("/reset")
    public ResultData reset(String orderId) {
        ResultData result = new ResultData();
        try {
            ResultData response = expressService.queryCodeValue(orderId);

            if (response.getResponseCode() != ResponseCode.RESPONSE_OK) {
                result.setResponseCode(response.getResponseCode());
                result.setDescription(response.getDescription());
                return result;
            }
            JSONArray jsonArray = JSON.parseArray(JSON.toJSONString(response.getData()));
            for (Object item : jsonArray) {
                JSONObject json = JSON.parseObject(JSON.toJSONString(item));
                String codeValue = json.getString("codeValue");
                response = installService.deleteAssign(codeValue);
            }
            response = expressService.deleteExpress(orderId);
            response = orderService.resetOrder(orderId);
        } catch (Exception e) {
            e.printStackTrace();
            result.setResponseCode(ResponseCode.RESPONSE_ERROR);
            result.setDescription(e.getMessage());
        }
        return result;
    }

    @PostMapping("/delete")
    public ResultData delete(String orderId) {
        ResultData result = new ResultData();
        try {
            ResultData response = expressService.queryCodeValue(orderId);

            if (response.getResponseCode() != ResponseCode.RESPONSE_OK) {
                result.setResponseCode(response.getResponseCode());
                result.setDescription(response.getDescription());
                return result;
            }

            List<Object> list = (List<Object>)response.getData();
            new Thread(() -> {
                ResultData rd = new ResultData();
                for (Object item : list) {
                    String codeValue = ((LinkedHashMap<String, Object>)item).get("codeValue").toString();
                    rd = installService.deleteAssign(codeValue);
                }
            }).start();

            new Thread(() -> {
               ResultData rd = expressService.deleteExpress(orderId);
          }).start();

            response = orderService.deleteOrder(orderId);
        } catch (Exception e) {
            e.printStackTrace();
            result.setResponseCode(ResponseCode.RESPONSE_ERROR);
            result.setDescription(e.getMessage());
        }
        return result;
    }
}
