package finley.gmair.controller;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import finley.gmair.form.drift.DriftOrderForm;
import finley.gmair.model.drift.*;
import finley.gmair.service.ActivityService;
import finley.gmair.service.EXCodeService;
import finley.gmair.service.EquipmentService;
import finley.gmair.service.OrderService;
import finley.gmair.util.ResponseCode;
import finley.gmair.util.ResultData;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.*;

import java.sql.Timestamp;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.util.*;

@RestController
@RequestMapping("/drift/order")
public class OrderController {

    @Autowired
    private OrderService orderService;

    @Autowired
    private ActivityService activityService;

    @Autowired
    private EXCodeService exCodeService;

    /**
     * The method is called to create order
     *
     * @return
     * */
    @PostMapping(value = "/create")
    public ResultData createDriftOrder(DriftOrderForm form) {
        ResultData result = new ResultData();
        if (StringUtils.isEmpty(form.getActivityId()) || StringUtils.isEmpty(form.getAddress()) || StringUtils.isEmpty(form.getConsignee())
            || StringUtils.isEmpty(form.getPhone()) || StringUtils.isEmpty(form.getItemList()) || StringUtils.isEmpty(form.getProvince())
            || StringUtils.isEmpty(form.getCity()) || StringUtils.isEmpty(form.getDistrict())) {
            result.setResponseCode(ResponseCode.RESPONSE_ERROR);
            result.setDescription("Please make sure you fill all the required fields");
            return result;
        }

        String activityId = form.getActivityId();
        Map<String, Object> condition = new HashMap<>();
        condition.put("activityId", activityId);
        ResultData response = activityService.fetchActivity(condition);
        if (response.getResponseCode() != ResponseCode.RESPONSE_OK) {
            result.setResponseCode(ResponseCode.RESPONSE_ERROR);
            result.setDescription("Activity match errors");
            return result;
        }
        Activity activity = ((List<Activity>) response.getData()).get(0);

        String activityName = activity.getActivityName();
        String consignee = form.getConsignee();
        String phone = form.getPhone();
        String address = form.getAddress();
        String orderNo = form.getOrderNo();
        String orderDate = form.getOrderDate();
        String province = form.getProvince();
        String city = form.getCity();
        String district = form.getDistrict();
        String description = form.getDescription();
        JSONArray itemList = JSONObject.parseArray(form.getItemList());
        List<DriftOrderItem> list = new ArrayList<>();

        //calculate order price
        double price = 0;
        for (Object Item : itemList) {
            DriftOrderItem orderItem = new DriftOrderItem();
            JSONObject json = JSON.parseObject(JSON.toJSONString(Item));
            String itemName = json.getString("commodityName");
            int quantity = json.getInteger("commodityQuantity");
            double itemPrice = json.getDouble("commodityPrice");
            orderItem.setItemName(itemName);
            orderItem.setQuantity(quantity);
            orderItem.setItemPrice(itemPrice);
            price += orderItem.getItemPrice() * orderItem.getQuantity();
            list.add(orderItem);
        }

        DriftOrder driftOrder = new DriftOrder(list, consignee, phone, address, orderNo, province, city, district, description, activityName);
        driftOrder.setTotalPrice(price);
        if (!StringUtils.isEmpty(form.getExcode())) {
            condition.clear();
            condition.put("codeValue", form.getExcode());
            condition.put("blockFlag", false);
            condition.put("status", 0);
            response = exCodeService.fetchEXCode(condition);
            if (response.getResponseCode() != ResponseCode.RESPONSE_OK) {
                result.setResponseCode(ResponseCode.RESPONSE_ERROR);
                result.setDescription("EXcode errors");
                return result;
            }
            EXCode exCode = ((List<EXCode>) response.getData()).get(0);
            double codePrice = exCode.getPrice();
            String codeId = exCode.getCodeId();
            double realPay = price - codePrice;
            driftOrder.setRealPay(realPay);
            driftOrder.setExcode(form.getExcode());
            new Thread(() -> {
                condition.clear();
                condition.put("codeId", codeId);
                condition.put("status", true);
                exCodeService.modifyEXCode(condition);
            }).start();
        } else {
            driftOrder.setRealPay(price);
        }

        //set time by orderDate
        DateTimeFormatter dateTimeFormatter = DateTimeFormatter.ofPattern("y-M-d");
        LocalDate localDate = LocalDate.parse(orderDate, dateTimeFormatter);
        LocalDateTime localDateTime = LocalDateTime.of(localDate, LocalTime.MIN);
        driftOrder.setCreateAt(Timestamp.valueOf(localDateTime));

        response = orderService.createDriftOrder(driftOrder);
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
     * The method is called to deal order when order is payed in bill
     *
     * @return
     * */
    @PostMapping(value = "/payed")
    public ResultData orderPayed(@RequestParam("orderId") String orderId) {
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
        order.setStatus(DriftOrderStatus.PAYED);
        response = orderService.updateDriftOrder(order);
        if (response.getResponseCode() != ResponseCode.RESPONSE_OK) {
            result.setResponseCode(ResponseCode.RESPONSE_ERROR);
            result.setDescription(new StringBuffer("Fail to update drift order with: ").append(order.toString()).toString());
            return result;
        }
        result.setResponseCode(ResponseCode.RESPONSE_OK);
        result.setDescription("Drift order is already payed");
        return result;
    }

    /**
     * The method is called to check the current order whether can be applied
     * 1. check the date in reservable days and in activity duration
     * 2. check the repository is enough to use
     *
     * @return
     * */
    @PostMapping(value = "/check")
    public ResultData check(String activityId, String selectDate) throws Exception {
        ResultData result = new ResultData();
        if (StringUtils.isEmpty(activityId) || StringUtils.isEmpty(selectDate)) {
            result.setResponseCode(ResponseCode.RESPONSE_ERROR);
            result.setDescription("Please make sure you fill all the required fields");
            return result;
        }
        Map<String, Object> condition = new HashMap<>();
        condition.put("activityId", activityId);
        condition.put("blockFlag", false);
        ResultData response = activityService.fetchActivity(condition);
        if (response.getResponseCode() != ResponseCode.RESPONSE_OK) {
            result.setResponseCode(ResponseCode.RESPONSE_ERROR);
            result.setDescription("System errors, please try again later");
            return result;
        }

        Activity activity = ((List<Activity>) response.getData()).get(0);
        double weekQuantity = activity.getRepositorySize() * activity.getThreshold();
        int reservableDays = activity.getReservableDays();
        Date endTime = activity.getEndTime();
        //check the date
        int compare = checkDate(selectDate, endTime, reservableDays);
        if (compare == -1) {
            result.setResponseCode(ResponseCode.RESPONSE_ERROR);
            result.setDescription("The select date is beyond access");
            return result;
        }

        int usedQuantity = checkQuantity(selectDate);
        if (usedQuantity == -1) {
            result.setResponseCode(ResponseCode.RESPONSE_ERROR);
            result.setDescription("System errors, please try again later");
        } else if (usedQuantity == 0) {
            result.setResponseCode(ResponseCode.RESPONSE_OK);
        } else {
            if (usedQuantity < weekQuantity) {
                result.setResponseCode(ResponseCode.RESPONSE_OK);
            } else {
                result.setResponseCode(ResponseCode.RESPONSE_ERROR);
                result.setDescription("Current orders have reached top");
            }
        }
        return result;
    }

    private int checkDate(String selectDate, Date endTime, int reservableDays) throws Exception {
        int flag = -1;
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(new Date());
        calendar.add(Calendar.DATE, reservableDays);
        System.out.print(calendar.getTime());
        Date terminalDate = sdf.parse(sdf.format(calendar.getTime()));
        Date select = sdf.parse(selectDate);
        Date end = sdf.parse(sdf.format(endTime));
        int compare = terminalDate.compareTo(end);
        if (compare > 0) {
            terminalDate = end;
        }
        compare = select.compareTo(terminalDate);
        if (compare > 0) {
            flag = -1;
            return flag;
        } else {
            flag = 1;
            return flag;
        }
    }

    private int checkQuantity(String selectDate) throws Exception {
        int quantity = 0;
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
        Date select = sdf.parse(selectDate);
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(select);
        calendar.add(Calendar.DATE, -3);
        Date weekStart = sdf.parse(sdf.format(calendar.getTime()));
        calendar.add(Calendar.DATE, 6);
        Date weekEnd = sdf.parse(sdf.format(calendar.getTime()));
        Map<String, Object> condition = new HashMap<>();
        condition.put("startTime", weekStart);
        condition.put("endTime", weekEnd);
        condition.put("blockFlag", false);
        ResultData response = orderService.fetchDriftOrder(condition);
        if (response.getResponseCode() == ResponseCode.RESPONSE_ERROR) {
            quantity = -1;
        }
        if (response.getResponseCode() == ResponseCode.RESPONSE_NULL) {
            quantity = 0;
        }
        if (response.getResponseCode() == ResponseCode.RESPONSE_OK) {
            List<DriftOrder> list = (List<DriftOrder>) response.getData();
            quantity = list.size();
        }
        return quantity;
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
            result.setDescription(new StringBuffer("Fail to confirm drift order with: ").append(order.toString()).toString());
            return result;
        }
        result.setResponseCode(ResponseCode.RESPONSE_OK);
        result.setDescription("Drift order is already confirmed");
        return result;
    }

    /**
     * The method is called to deliver order
     *
     * @return
     * */
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
                case "PAYED":
                    condition.put("status", 1);
                    break;
                case "CONFIRMED":
                    condition.put("status", 2);
                    break;
                case "DELIVERED":
                    condition.put("status", 3);
                    break;
                case "FINISHED":
                    condition.put("status", 4);
                    break;
                case "CLOSED":
                    condition.put("status", 5);
                    break;
                case "CANCELED":
                    condition.put("status", 6);
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
