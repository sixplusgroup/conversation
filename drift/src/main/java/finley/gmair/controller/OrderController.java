package finley.gmair.controller;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import finley.gmair.model.drift.DriftOrder;
import finley.gmair.model.drift.DriftOrderItem;
import finley.gmair.service.OrderService;
import finley.gmair.util.ResponseCode;
import finley.gmair.util.ResultData;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.sql.Timestamp;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;

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
        DriftOrderItem item = new DriftOrderItem();
        for (Object orderItem : orderItemList) {
            JSONObject json = JSON.parseObject(JSON.toJSONString(orderItem));
            String itemName = json.getString("commodityName");
            int quantity = json.getInteger("commodityQuantity");
            double itemPrice = json.getDouble("commodityPrice");
            String testTarget = json.getString("testTarget");
            item.setItemName(itemName);
            item.setQuantity(quantity);
            item.setItemPrice(itemPrice);
            item.setTestTarget(testTarget);
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
}
