package finley.gmair.controller;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import finley.gmair.model.drift.DriftOrder;
import finley.gmair.model.drift.DriftOrderItem;
import finley.gmair.model.drift.DriftOrderStatus;
import finley.gmair.service.OrderService;
import finley.gmair.util.ResponseCode;
import finley.gmair.util.ResultData;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.*;

import java.sql.Timestamp;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/drift/order")
public class OrderController {

    @Autowired
    private OrderService orderService;

    @PostMapping(value = "/create")
    public ResultData createDriftOrder(String order) {
        ResultData result = new ResultData();

        JSONObject jsonObject = JSONObject.parseObject(order);
        String consignee = jsonObject.getString("consignee");
        String phone = jsonObject.getString("phone");
        String address = jsonObject.getString("address");
        String orderNo = jsonObject.getString("orderNo");
        String orderDate = jsonObject.getString("orderDate");
        String province = jsonObject.getString("province");
        String city = jsonObject.getString("city");
        String district = jsonObject.getString("district");
        String description = jsonObject.getString("description");
        double price = jsonObject.getDouble("price");
        double pay = jsonObject.getDouble("pay");

        JSONArray orderItemList = jsonObject.getJSONArray("orderItemList");
        List<DriftOrderItem> list = new ArrayList<>();
        for (Object orderItem : orderItemList) {
            DriftOrderItem item = new DriftOrderItem();
            JSONObject json = JSON.parseObject(JSON.toJSONString(orderItem));
            String itemName = json.getString("commodityName");
            int quantity = json.getInteger("commodityQuantity");
            double itemPrice = json.getDouble("commodityPrice");
            item.setItemName(itemName);
            item.setQuantity(quantity);
            item.setItemPrice(itemPrice);
            list.add(item);
        }

        //build drift order
        DriftOrder driftOrder = new DriftOrder(list, consignee, phone, address, orderNo, province, city, district, description, pay);
        driftOrder.setTotalPrice(price);

        //set time by orderDate
        DateTimeFormatter dateTimeFormatter = DateTimeFormatter.ofPattern("y-M-d");
        LocalDate localDate = LocalDate.parse(orderDate, dateTimeFormatter);
        LocalDateTime localDateTime = LocalDateTime.of(localDate, LocalTime.MIN);
        driftOrder.setCreateAt(Timestamp.valueOf(localDateTime));

        ResultData response = orderService.createDriftOrder(driftOrder);
        if (response.getResponseCode() == ResponseCode.RESPONSE_ERROR) {
            result.setResponseCode(ResponseCode.RESPONSE_ERROR);
            result.setDescription("服务器正忙，请稍后再试");
        } else if (response.getResponseCode() == ResponseCode.RESPONSE_NULL) {
            result.setResponseCode(ResponseCode.RESPONSE_NULL);
            result.setDescription("无相关数据，请仔细检查");
        } else {
            result.setResponseCode(ResponseCode.RESPONSE_OK);
            result.setData(response.getData());
        }
        return result;
    }

    /**
     * The function is called  to delete the order with orderId
     *
     * @return
     * */
    @PostMapping(value = "/delete")
    public ResultData deleteOrder(String orderId) {
        ResultData result = new ResultData();
        if (StringUtils.isEmpty(orderId)) {
            result.setResponseCode(ResponseCode.RESPONSE_ERROR);
            result.setDescription("please make sure you fill all the required fields");
            return result;
        }

        ResultData response = orderService.deleteDriftOrder(orderId);
        if (response.getResponseCode() == ResponseCode.RESPONSE_ERROR) {
            result.setResponseCode(ResponseCode.RESPONSE_ERROR);
            result.setDescription("Fail to delete drift order");
        }
        if (response.getResponseCode() == ResponseCode.RESPONSE_OK){
            result.setResponseCode(ResponseCode.RESPONSE_OK);
            result.setDescription("Succeed to delete drift order");
        }
        return result;
    }

    /**
     * The function is called to confirm the order whether can be accepted or not
     *
     * @return
     * */
    @PostMapping(value = "/confirm")
    public ResultData orderConfirm(@RequestParam("orderId") String orderId) {
        ResultData result = new ResultData();
        Map<String, Object> condition = new HashMap<>();
        condition.put("orderId", orderId);
        condition.put("blockFlag", false);
        ResultData response = orderService.fetchDriftOrder(condition);
        if (response.getResponseCode() == ResponseCode.RESPONSE_ERROR) {
            result.setResponseCode(ResponseCode.RESPONSE_ERROR);
            result.setDescription(new StringBuffer("Fail to retrieve drift order with orderId: ").append(orderId).toString());
            return result;
        }
        if (response.getResponseCode() == ResponseCode.RESPONSE_NULL) {
            result.setResponseCode(ResponseCode.RESPONSE_NULL);
            result.setDescription(new StringBuffer("The drift order with orderId: ").append(orderId).append(" doesn't exist").toString());
            return result;
        }

        DriftOrder order = ((List<DriftOrder>) response.getData()).get(0);
        order.setStatus(DriftOrderStatus.CONFIRMED);
        response = orderService.updateDriftOrder(order);
        if (response.getResponseCode() != ResponseCode.RESPONSE_OK) {
            result.setResponseCode(ResponseCode.RESPONSE_ERROR);
            result.setDescription(new StringBuffer("Fail to update drift order with: ").append(order.toString()).toString());
            return result;
        }
        result.setResponseCode(ResponseCode.RESPONSE_OK);
        result.setDescription("Drift order update successfully");
        return result;
    }

    /**
     * The method is called to deliver order
     *
     * @return */
    @PostMapping(value = "/deliver")
    public ResultData orderDeliver(@RequestParam("orderId") String orderId) {
        ResultData result = new ResultData();
        Map<String, Object> condition = new HashMap<>();
        condition.put("orderId", orderId);
        condition.put("blockFlag", false);
        ResultData response = orderService.fetchDriftOrder(condition);
        if (response.getResponseCode() == ResponseCode.RESPONSE_ERROR) {
            result.setResponseCode(ResponseCode.RESPONSE_ERROR);
            result.setDescription(new StringBuffer("Fail to retrieve drift order with orderId: ").append(orderId).toString());
            return result;
        }
        if (response.getResponseCode() == ResponseCode.RESPONSE_NULL) {
            result.setResponseCode(ResponseCode.RESPONSE_NULL);
            result.setDescription(new StringBuffer("The drift order with orderId: ").append(orderId).append(" doesn't exist").toString());
            return result;
        }

        DriftOrder order = ((List<DriftOrder>) response.getData()).get(0);
        order.setStatus(DriftOrderStatus.DELIVERED);
        response = orderService.updateDriftOrder(order);
        if (response.getResponseCode() != ResponseCode.RESPONSE_OK) {
            result.setResponseCode(ResponseCode.RESPONSE_ERROR);
            result.setDescription(new StringBuffer("Fail to update drift order with: ").append(order.toString()).toString());
        } else {
            result.setResponseCode(ResponseCode.RESPONSE_OK);
        }
        return result;
    }

    /**
     * The method is called to get order information by useful conditions
     * contains time, province, city and so on
     *
     * @return
     * */
    @GetMapping(value = "/list")
    public ResultData orderList(String startTime, String endTime, String provinceName, String cityName, String status) {
        ResultData result = new ResultData();
        Map<String, Object> condition = new HashMap<>();
        condition.put("blockFlag", false);
        if (!StringUtils.isEmpty(startTime)) {
            condition.put("startTime", startTime);
        }
        if (!StringUtils.isEmpty(endTime)) {
            condition.put("endTime", endTime);
        }
//        if (!StringUtils.isEmpty(provinceName)) {
//            condition.put("provinceName", provinceName);
//        }
//        if (!StringUtils.isEmpty(cityName)) {
//            condition.put("cityName", cityName);
//        }
        if (!StringUtils.isEmpty(status)) {
            switch (status) {
                case "APPLIED":
                    condition.put("status", 0);
                    break;
                case "PROCESSED":
                    condition.put("status", 1);
                    break;
                case "DELIVERED":
                    condition.put("status", 2);
                    break;
                case "FINISHED":
                    condition.put("status", 3);
                    break;
                case "CLOSED":
                    condition.put("status", 4);
                    break;
                case "CANCELED":
                    condition.put("status", 5);
                    break;
            }
        }

        ResultData response = orderService.fetchDriftOrder(condition);
        switch (response.getResponseCode()) {
            case RESPONSE_NULL:
                result.setResponseCode(ResponseCode.RESPONSE_NULL);
                result.setDescription("No drift order");
                break;
            case RESPONSE_ERROR:
                result.setResponseCode(ResponseCode.RESPONSE_ERROR);
                result.setDescription("Query error, please try again later");
                break;
            case RESPONSE_OK:
                result.setResponseCode(ResponseCode.RESPONSE_OK);
                result.setData(response.getData());
                break;
        }
        return result;
    }
}
