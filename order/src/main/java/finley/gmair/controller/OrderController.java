package finley.gmair.controller;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import finley.gmair.model.location.OrderLocationRetryCount;
import finley.gmair.model.order.OrderItem;
import finley.gmair.model.order.OrderStatus;
import finley.gmair.model.order.PlatformOrder;
import finley.gmair.service.*;
import finley.gmair.util.ResponseCode;
import finley.gmair.util.ResultData;
import finley.gmair.vo.location.OrderLocationRetryCountVo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.multipart.MultipartHttpServletRequest;

import java.sql.Timestamp;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.util.*;

@RequestMapping("/order")
@RestController
public class OrderController {
    @Autowired
    private OrderService orderService;

    @Autowired
    private ExpressService expressService;

    @Autowired
    private InstallService installService;

    @Autowired
    private ReconnaissanceService reconnaissanceService;

    @Autowired
    private LocationService locationService;

    @Autowired
    private OrderLocationRetryCountService orderLocationRetryCountService;

    /**
     * This method is aimed to handle the order spreadsheet and store the records
     *
     * @param
     * @return
     */
    @RequestMapping(method = RequestMethod.POST, value = "/upload", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResultData upload(MultipartHttpServletRequest request) {
        ResultData result = new ResultData();
        ResultData response = orderService.process(request.getFile("order_list"));
        if (response.getResponseCode() == ResponseCode.RESPONSE_ERROR) {
            result.setResponseCode(ResponseCode.RESPONSE_ERROR);
            result.setDescription("Fail to process the file");
            return result;
        }
        result.setResponseCode(ResponseCode.RESPONSE_OK);
        result.setDescription("Orders are in process, please check it later");
        return result;
    }

    @RequestMapping(method = RequestMethod.POST, value = "/create")
    public ResultData create(String order) {
        ResultData result = new ResultData();

        List<OrderItem> list = new ArrayList<>();
        JSONObject jsonObject = JSONObject.parseObject(order);
        String channel = jsonObject.getString("channel");
        String orderNo = jsonObject.getString("orderNo");
        String orderDate = jsonObject.getString("orderDate");
        String consignee = jsonObject.getString("consignee");
        String phone = jsonObject.getString("phone");
        String province = jsonObject.getString("province");
        String city = jsonObject.getString("city");
        String district = jsonObject.getString("district");
        String address = jsonObject.getString("address");
        double price = jsonObject.getDouble("price");
        String description = jsonObject.getString("description");

        PlatformOrder platformOrder = new PlatformOrder(list, orderNo, consignee, phone, address, channel, description);
        platformOrder.setTotalPrice(price);
        platformOrder.setLocation(province, city, district);

        //设置时间
        DateTimeFormatter dateTimeFormatter = DateTimeFormatter.ofPattern("y-M-d");
        LocalDate localDate = LocalDate.parse(orderDate, dateTimeFormatter);
        LocalDateTime localDateTime = LocalDateTime.of(localDate, LocalTime.MIN);
        platformOrder.setCreateAt(Timestamp.valueOf(localDateTime));

        //解析地址
        ResultData locationResult = locationService.geocoder(address);
        if (locationResult.getResponseCode() == ResponseCode.RESPONSE_OK) {
            JSONObject location = (JSON.parseObject(JSON.toJSONString(locationResult.getData()))).getJSONObject("location");
            double latitude = 0.0, longitude = 0.0;
            try {
                latitude = Double.parseDouble(location.getString("lat"));
                longitude = Double.parseDouble(location.getString("lng"));
            } catch (Exception e) {

            }
            platformOrder.setLatitude(latitude);
            platformOrder.setLongitude(longitude);
        } else {
            orderService.insertOrderLocationRetryCount(platformOrder.getOrderId(), 1);
        }

        JSONArray orderItemList = jsonObject.getJSONArray("orderItemList");
        boolean containsMachine = false;
        for (Object orderItem : orderItemList) {
            OrderItem item = new OrderItem();
            String commodityType = ((JSONObject) orderItem).getString("commodityType");
            if (!containsMachine && commodityType.equals("GUOMAI_XINFENG")) {
                containsMachine = true;
            }
            String commodityId = ((JSONObject) orderItem).getString("commodityId");
            String itemName = ((JSONObject) orderItem).getString("commodityName");
            int quantity = ((JSONObject) orderItem).getInteger("commodityQuantity");
            double itemPrice = ((JSONObject) orderItem).getDouble("commodityPrice");
            item.setCommodityId(commodityId);
            item.setItemName(itemName);
            item.setQuantity(quantity);
            item.setItemPrice(itemPrice);
            list.add(item);
        }

        ResultData response = orderService.createPlatformOrder(platformOrder);
        if (response.getResponseCode() == ResponseCode.RESPONSE_ERROR) {
            result.setResponseCode(ResponseCode.RESPONSE_ERROR);
            result.setDescription("服务器忙，请稍后再试");
        } else if (response.getResponseCode() == ResponseCode.RESPONSE_NULL) {
            result.setResponseCode(ResponseCode.RESPONSE_NULL);
            result.setDescription("无相关数据");
        } else {
            result.setData(response.getData());
        }
        //if order contains a machine
        if (containsMachine) {
            try {
                PlatformOrder orderResponse = (PlatformOrder) response.getData();
                response = reconnaissanceService.createReconnaissance(orderResponse.getOrderId());
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
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
        JSONObject data = (JSONObject) JSON.toJSON(((List<PlatformOrder>) response.getData()).get(0));

        // get install assign info
        try {
            response = expressService.queryCodeValue(orderId);
            if (response.getResponseCode() == ResponseCode.RESPONSE_OK) {
                ArrayList<Object> jsonArray = (ArrayList<Object>) response.getData();
                JSONArray assignArray = new JSONArray();
                for (Object jsonObject : jsonArray) {
                    String qrcode = (new JSONObject((LinkedHashMap) jsonObject)).getString("codeValue");
                    ResultData assignResponse = installService.getAssign(qrcode);
                    if (assignResponse.getResponseCode() == ResponseCode.RESPONSE_OK) {
                        assignArray.add(((ArrayList) assignResponse.getData()).get(0));
                    }
                }
                data.put("installAssign", assignArray);
            } else {
                data.put("installAssign", new JSONArray());
            }
        } catch (Exception e) {
            e.printStackTrace();
            data.put("installAssign", new JSONArray());
        }

        // get express info
        try {
            response = expressService.queryExpress(orderId);
            if (response.getResponseCode() == ResponseCode.RESPONSE_OK) {
                data.put("expressInfo", new JSONObject((LinkedHashMap) response.getData()));
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        //get reconnaissance info
        try {
            response = reconnaissanceService.getReconnaissance(orderId);
            if (response.getResponseCode() == ResponseCode.RESPONSE_OK) {
                data.put("reconnaissanceInfo", new JSONObject((LinkedHashMap) response.getData()));
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        result.setData(data);
        return result;
    }

    @RequestMapping(method = RequestMethod.GET, value = "/list")
    public ResultData list(String startTime, String endTime, String provinceName, String cityName, String status) {
        ResultData result = new ResultData();
        Map<String, Object> condition = new HashMap<>();
        condition.put("blockFlag", false);
        if (!StringUtils.isEmpty(startTime))
            condition.put("startTime", startTime);
        if (!StringUtils.isEmpty(endTime))
            condition.put("endTime", endTime);
        if (!StringUtils.isEmpty(provinceName))
            condition.put("provinceName", "%" + provinceName + "%");
        if (!StringUtils.isEmpty(cityName)) {
            condition.put("cityName", "%" + cityName + "%");
        }
        if (!StringUtils.isEmpty(status)) {
            switch (status) {
                case "PAYED":
                    condition.put("status", 0);
                    break;
                case "PROCESSING":
                    condition.put("status", 1);
                    break;
                case "FINISHED":
                    condition.put("status", 2);
                    break;
                case "COMMENTED":
                    condition.put("status", 3);
                    break;
                case "CLOSED":
                    condition.put("status", 4);
                    break;
            }

        }
        ResultData response = orderService.fetchPlatformOrder(condition);
        if (response.getResponseCode() == ResponseCode.RESPONSE_NULL) {
            result.setResponseCode(ResponseCode.RESPONSE_NULL);
            result.setDescription("no more order list");
            return result;
        }
        if (response.getResponseCode() == ResponseCode.RESPONSE_ERROR) {
            result.setResponseCode(ResponseCode.RESPONSE_ERROR);
            result.setDescription("Fail to retrieve the order information, please try again later");
            return result;
        }
        result.setResponseCode(ResponseCode.RESPONSE_OK);
        result.setData(response.getData());
        return result;
    }

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
        } else {
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
            result.setData(((List<PlatformOrder>) response.getData()).get(0));
            return result;
        }
        return result;
    }

    @RequestMapping(method = RequestMethod.POST, value = "/installCreation")
    public ResultData orderInstall(@RequestParam("orderId") String orderId,
                                   @RequestParam(value = "qrcode", required = false) String qrcode
    ) {
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

    @RequestMapping(method = RequestMethod.POST, value = "/deliver")
    public ResultData orderDeliver(@RequestParam("orderId") String orderId) {
        ResultData result = new ResultData();
        Map<String, Object> condition = new HashMap<>();
        condition.put("orderId", orderId);
        condition.put("blockFlag", false);
        ResultData response = orderService.fetchPlatformOrder(condition);
        if (response.getResponseCode() == ResponseCode.RESPONSE_ERROR) {
            result.setResponseCode(ResponseCode.RESPONSE_ERROR);
            result.setDescription(new StringBuffer("Fail to retrieve order information for order: ").append(orderId).toString());
            return result;
        } else if (response.getResponseCode() == ResponseCode.RESPONSE_NULL) {
            result.setResponseCode(ResponseCode.RESPONSE_NULL);
            result.setDescription(new StringBuffer("Order: ").append(orderId).append(" does not exist.").toString());
            return result;
        }
        PlatformOrder order = ((List<PlatformOrder>) response.getData()).get(0);
        order.setStatus(OrderStatus.PROCESSING);
        response = orderService.updatePlatformOrder(order);
        if (response.getResponseCode() != ResponseCode.RESPONSE_OK) {
            result.setResponseCode(ResponseCode.RESPONSE_ERROR);
            result.setDescription("更新订单状态失败");
            return result;
        }

        return result;
    }

    @PostMapping(value = "/delete")
    public ResultData deleteOrder(String orderId) {
        ResultData result = new ResultData();
        if (StringUtils.isEmpty(orderId)) {
            result.setResponseCode(ResponseCode.RESPONSE_ERROR);
            result.setDescription("please make sure you fill the required fields");
            return result;
        }
        ResultData response = orderService.deletePlatformOrder(orderId);
        switch (response.getResponseCode()) {
            case RESPONSE_ERROR:
                result.setResponseCode(ResponseCode.RESPONSE_ERROR);
                result.setDescription("Fail to delete order");
            case RESPONSE_OK:
                result.setResponseCode(ResponseCode.RESPONSE_OK);
                result.setDescription("Succeed to delete order");
        }
        return result;
    }

    @PostMapping(value = "/reset")
    public ResultData resetOrder(String orderId) {
        ResultData result = new ResultData();
        if (StringUtils.isEmpty(orderId)) {
            result.setResponseCode(ResponseCode.RESPONSE_ERROR);
            result.setDescription("please make sure you fill the required fields");
            return result;
        }
        Map<String, Object> condition = new HashMap<>();
        condition.put("orderId", orderId);
        condition.put("status", OrderStatus.PAYED.getValue());
        ResultData response = orderService.resetPlatformOrder(condition);
        switch (response.getResponseCode()) {
            case RESPONSE_ERROR:
                result.setResponseCode(ResponseCode.RESPONSE_ERROR);
                result.setDescription("Fail to reset order");
            case RESPONSE_OK:
                result.setResponseCode(ResponseCode.RESPONSE_OK);
                result.setDescription("Succeed to reset order");
        }
        return result;
    }
}
