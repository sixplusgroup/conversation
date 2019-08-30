package finley.gmair.controller;

import com.alibaba.fastjson.JSONObject;
import finley.gmair.form.drift.DriftOrderForm;
import finley.gmair.model.drift.*;
import finley.gmair.service.*;
import finley.gmair.util.IPUtil;
import finley.gmair.util.ResponseCode;
import finley.gmair.util.ResultData;
import finley.gmair.util.StringUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
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

    @Autowired
    private AttachmentService attachmentService;

    @Autowired
    private EquipmentService equipmentService;

    @Autowired
    private PaymentService paymentService;

    @Autowired
    private ExpressService expressService;

    private Object lock = new Object();


    /**
     * Once user click on the submit form, the request will be forwarded here to process the order
     *
     * @param form
     * @return
     * @throws Exception
     */
    @PostMapping(value = "/create")
    public ResultData createDriftOrder(DriftOrderForm form, HttpServletRequest request) throws Exception {
        ResultData result = new ResultData();
        // activityId is the unique identification for an activity
        // consumerId is the openid for a wechat user in this mini program
        // equipid is the unique identification for the equipment used in the activity
        // address is the place where we will deliver/go to for the check
        // consignee is the name of the applicant
        // phone is the contact phone number of the consignee
        // expectedDate is an optional argument for an apply, if enabled in activity
        // intervalDate indicates how long the consignee can keep the device
        if (StringUtil.isEmpty(form.getActivityId(), form.getConsumerId(), form.getConsignee(), form.getEquipId(), form.getProvince(), form.getCity(), form.getDistrict())) {
            result.setResponseCode(ResponseCode.RESPONSE_ERROR);
            result.setDescription("Please make sure you fill all the required fields");
            return result;
        }
        String ip = IPUtil.getIP(request);
        String activityId = form.getActivityId();
        Map<String, Object> condition = new HashMap<>();
        condition.put("activityId", activityId);
        condition.put("blockFlag", false);
        ResultData response = activityService.fetchActivity(condition);
        if (response.getResponseCode() != ResponseCode.RESPONSE_OK) {
            result.setResponseCode(ResponseCode.RESPONSE_ERROR);
            result.setDescription("未能查询到与".concat(form.getActivityId()).concat("相关的活动"));
            return result;
        }
        //获取活动的信息
        Activity activity = ((List<Activity>) response.getData()).get(0);
        int intervalDate = activity.getReservableDays();
        if (intervalDate != form.getIntervalDate()) {
            result.setResponseCode(ResponseCode.RESPONSE_ERROR);
            result.setDescription("活动信息发生变更，请重新申请");
            return result;
        }
        String equipId = form.getEquipId();
        // 检查设备的ID是否与活动存在关联
        condition.clear();
        condition.put("activityId", activityId);
        condition.put("equipId", equipId);
        condition.put("blockFlag", false);
        response = activityService.fetchActivityEquipment(condition);
        if (response.getResponseCode() != ResponseCode.RESPONSE_OK) {
            result.setResponseCode(ResponseCode.RESPONSE_ERROR);
            result.setDescription("[Error]未能查询到活动和设备的关联信息，请稍后尝试");
            return result;
        }
        // 查询设备是否存在
        condition.clear();
        condition.put("equipId", equipId);
        condition.put("blockFlag", false);
        response = equipmentService.fetchEquipment(condition);
        if (response.getResponseCode() != ResponseCode.RESPONSE_OK) {
            result.setResponseCode(ResponseCode.RESPONSE_ERROR);
            result.setDescription("[Error]未能查询到该设备，请稍后尝试");
            return result;
        }
        // 开始处理用户的申请
        String activityName = activity.getActivityName();
        String consumerId = form.getConsumerId();
        String consignee = form.getConsignee();
        String phone = form.getPhone();
        String address = form.getAddress();
        String province = form.getProvince();
        String city = form.getCity();
        String district = form.getDistrict();
        String expectedDate = form.getExpectedDate();
        String description = form.getDescription();

        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
        Date expected = sdf.parse(expectedDate);
        Equipment equipment = ((List<Equipment>) response.getData()).get(0);
        //todo 检查当天是否可以继续借出设备

        //构建订单中的子订单项
        List<DriftOrderItem> list = new ArrayList<>();
        DriftOrderItem equipItem = new DriftOrderItem();

        equipItem.setItemName(equipment.getEquipName());
        equipItem.setItemPrice(equipment.getEquipPrice());
        equipItem.setQuantity(1);
        list.add(equipItem);
        response = attachmentService.fetch(condition);
        if (response.getResponseCode() == ResponseCode.RESPONSE_OK) {
            Attachment attachment = ((List<Attachment>) response.getData()).get(0);
            DriftOrderItem attachItem = new DriftOrderItem();
            attachItem.setItemName(attachment.getAttachName());
            attachItem.setItemPrice(attachment.getAttachPrice());
            attachItem.setQuantity(form.getItemQuantity());
            list.add(attachItem);
        }

        double price = 0;
        // 计算该订单的总价格
        for (DriftOrderItem orderItem : list) {
            price += (orderItem.getItemPrice()) * orderItem.getQuantity();
        }

        DriftOrder driftOrder = new DriftOrder(consumerId, equipId, consignee, phone, address, province, city, district, description, activityId, expected, intervalDate);
        driftOrder.setTotalPrice(price);
        driftOrder.setList(list);
        // 若订单使用了优惠券
        if (!StringUtils.isEmpty(form.getExcode())) {
            condition.clear();
            condition.put("codeValue", form.getExcode());
            condition.put("blockFlag", false);
            condition.put("status", 1);
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
                condition.put("status", 2);
                exCodeService.modifyEXCode(condition);
            }).start();
        } else {
            driftOrder.setRealPay(price);
        }
        response = orderService.createDriftOrder(driftOrder);
        if (response.getResponseCode() == ResponseCode.RESPONSE_ERROR) {
            result.setResponseCode(ResponseCode.RESPONSE_ERROR);
            result.setDescription("服务器正忙，请稍后再试");
        } else if (response.getResponseCode() == ResponseCode.RESPONSE_NULL) {
            result.setResponseCode(ResponseCode.RESPONSE_NULL);
            result.setDescription("无相关数据，请仔细检查");
        } else {
            result.setResponseCode(ResponseCode.RESPONSE_OK);
            if ((int) (driftOrder.getRealPay() * 100) == 0) {
                //todo 若订单金额为0，则自动更新订单状态为已付款
            } else {
                new Thread(() -> paymentService.createPay(driftOrder.getOrderId(), consumerId, (int) (driftOrder.getRealPay() * 100), activityName, ip)).start();
            }
            result.setData(response.getData());
        }
        return result;
    }

    /**
     * The function is called  to delete the order with orderId
     *
     * @return
     */
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
        if (response.getResponseCode() == ResponseCode.RESPONSE_OK) {
            result.setResponseCode(ResponseCode.RESPONSE_OK);
            result.setDescription("Succeed to delete drift order");
        }
        return result;
    }

    /**
     * The method is called to deal order when order is payed in bill
     *
     * @return
     */
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
     */
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
     */
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
     */
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
     */
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

    /**
     * 根据活动ID查询当前活动的概述，包括活动当前报名人数
     * todo
     *
     * @param activityId
     * @return
     */
    @GetMapping("/summary")
    public ResultData summary(String activityId) {
        ResultData result = new ResultData();
        JSONObject json = new JSONObject();
        Map<String, Object> condition = new HashMap<>();
        condition.put("activityId", activityId);
        condition.put("blockFlag", false);
        ResultData response = orderService.fetchDriftOrder(condition);
        if (response.getResponseCode() == ResponseCode.RESPONSE_NULL) {
            json.put("size", 0);
        }
        if (response.getResponseCode() == ResponseCode.RESPONSE_OK) {
            json.put("size", ((List) response.getData()).size());
        }
        result.setData(json);
        if (response.getResponseCode() == ResponseCode.RESPONSE_ERROR) {
            result.setResponseCode(ResponseCode.RESPONSE_ERROR);
            result.setDescription("未能够成功查询到活动信息");
        }
        return result;
    }

    @GetMapping("/{orderId}")
    public ResultData info(@PathVariable("orderId") String orderId) {
        ResultData result = new ResultData();
        Map<String, Object> condition = new HashMap<>();
        condition.put("orderId", orderId);
        condition.put("blockFlag", false);
        ResultData response = orderService.fetchDriftOrder(condition);
        if (response.getResponseCode() == ResponseCode.RESPONSE_ERROR) {
            result.setResponseCode(ResponseCode.RESPONSE_ERROR);
            result.setDescription("获取订单信息失败，请稍后尝试");
            return result;
        }
        if (response.getResponseCode() == ResponseCode.RESPONSE_NULL) {
            result.setResponseCode(ResponseCode.RESPONSE_NULL);
            result.setDescription("当前未查询到order id为".concat(orderId).concat("的信息"));
            return result;
        }
        if (response.getResponseCode() == ResponseCode.RESPONSE_OK) {
            result.setData(((List) response.getData()).get(0));
        }
        return result;
    }

    @GetMapping("/{openid}/list")
    public ResultData list(@PathVariable("openid") String openid) {
        ResultData result = new ResultData();
        if (StringUtil.isEmpty(openid)) {
            result.setResponseCode(ResponseCode.RESPONSE_ERROR);
            result.setDescription("请提供所需要获取订单的用户的信息");
            return result;
        }
        Map<String, Object> condition = new HashMap<>();
        condition.put("consumerId", openid);
        condition.put("blockFlag", false);
        ResultData response = orderService.fetchDriftOrder(condition);
        if (response.getResponseCode() == ResponseCode.RESPONSE_ERROR) {
            result.setResponseCode(ResponseCode.RESPONSE_ERROR);
            result.setDescription("未能查询用户订单信息，请稍后尝试");
        }
        if (response.getResponseCode() == ResponseCode.RESPONSE_NULL) {
            result.setResponseCode(ResponseCode.RESPONSE_NULL);
            result.setDescription("当前用户暂无订单信息");
        }
        if (response.getResponseCode() == ResponseCode.RESPONSE_OK) {
            result.setData(response.getData());
        }
        return result;
    }

    /**
     * The method is called to create OrderExpress and update the status of the order
     *
     * @param orderId
     * @param expressId
     * @param expressFlag 0-->delivered ;1-->back ;other value is wrong
     * @param company
     * @return
     */
    @PostMapping(value = "/express/create")
    public ResultData createOrderExpress(String orderId, String expressId, int expressFlag, String company){
        ResultData result = new ResultData();
        if(StringUtil.isEmpty(orderId,expressId,company)){
            result.setResponseCode(ResponseCode.RESPONSE_ERROR);
            result.setDescription("Please make sure you fill all the required fields");
            return result;
        }
        Map<String, Object> condition = new HashMap<>();
        condition.put("orderId",orderId);
        condition.put("blockFlag", false);
        ResultData response1 = orderService.fetchDriftOrder(condition);
        if (response1.getResponseCode() != ResponseCode.RESPONSE_OK) {
            result.setResponseCode(ResponseCode.RESPONSE_ERROR);
            result.setDescription("未能查询到与".concat(orderId).concat("相关的订单"));
            return result;
        }

        //update order status
        Express express = new Express(orderId, expressId, company);
        express.setStatus(ExpressStatus.valueOf(expressFlag));
        if(express.getStatus()==null){
            result.setResponseCode(ResponseCode.RESPONSE_ERROR);
            result.setDescription("订单标识异常");
            return result;
        }
        else{
            DriftOrder order = ((List<DriftOrder>) response1.getData()).get(0);
            if(express.getStatus() == ExpressStatus.DELIVERED)
                order.setStatus(DriftOrderStatus.DELIVERED);
            else if(express.getStatus() == ExpressStatus.BACk)
                order.setStatus(DriftOrderStatus.BACK);
            response1 = orderService.updateDriftOrder(order);
            if (response1.getResponseCode() != ResponseCode.RESPONSE_OK) {
                result.setResponseCode(ResponseCode.RESPONSE_ERROR);
                result.setDescription(new StringBuffer("Fail to update drift order with: ").append(order.toString()).toString());
                return result;
            }
        }

        //create express
        ResultData response2 = expressService.createExpress(express);
        if (response2.getResponseCode() == ResponseCode.RESPONSE_ERROR) {
            result.setResponseCode(ResponseCode.RESPONSE_ERROR);
            result.setDescription("Fail to store express message to database");
            return result;
        }
        if (response2.getResponseCode() == ResponseCode.RESPONSE_OK) {
            result.setResponseCode(ResponseCode.RESPONSE_OK);
            result.setData(response2.getData());
        }
        return result;
    }
}
