package finley.gmair.controller;

import com.alibaba.fastjson.JSONObject;
import finley.gmair.form.drift.DriftOrderForm;
import finley.gmair.model.drift.*;
import finley.gmair.model.drift.DriftExpress;
import finley.gmair.model.express.Express;
import finley.gmair.model.order.OrderStatus;
import finley.gmair.service.*;
import finley.gmair.util.IPUtil;
import finley.gmair.util.ResponseCode;
import finley.gmair.util.ResultData;
import finley.gmair.util.StringUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.text.SimpleDateFormat;
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

    @Autowired
    private ExpressAgentService expressAgentService;

    @Autowired
    private MachineService machineService;

    @Autowired
    private QrExCodeService qrExCodeService;

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
        //检查当天是否可以继续借出设备
        if(!available(activityId, expected)){
            result.setResponseCode(ResponseCode.RESPONSE_ERROR);
            result.setDescription("该日期仪器已被预定完，请重新选择日期");
            return result;
        }

        //构建订单中的子订单项
        List<DriftOrderItem> list = new ArrayList<>();
        DriftOrderItem equipItem = new DriftOrderItem();

        equipItem.setItemName(equipment.getEquipName());
        equipItem.setItemPrice(equipment.getEquipPrice());
        equipItem.setSingleNum(1);
        equipItem.setQuantity(1);
        list.add(equipItem);
        //处理订单中的试纸子项
        JSONObject attachItems = JSONObject.parseObject(form.getAttachItem());
        for (Map.Entry<String, Object> e : attachItems.entrySet()) {
            condition.put("attachId", e.getKey());
            response = attachmentService.fetch(condition);
            if ((response.getResponseCode() == ResponseCode.RESPONSE_OK) && (((Integer) e.getValue()).intValue() != 0)) {
                Attachment attachment = ((List<Attachment>) response.getData()).get(0);
                DriftOrderItem attachItem = new DriftOrderItem();
                attachItem.setItemName(attachment.getAttachName());
                attachItem.setItemPrice(attachment.getAttachPrice());
                attachItem.setSingleNum(attachment.getAttachSingle());
                int num = ((Integer) e.getValue()).intValue();
                num = num + num/5;
                attachItem.setQuantity(num);
                list.add(attachItem);
            }
        }

        double price = 0;
        // 计算该订单的总价格
        for (DriftOrderItem orderItem : list) {
            int quantity = orderItem.getQuantity();
            quantity = quantity - (quantity-1)/5;
            price += (orderItem.getItemPrice()) * quantity;
        }
        //查询同一消费者的订单，检查预计使用日期在15天内添加提示
        condition.clear();
        int[] statusList = new int[]{DriftOrderStatus.PAYED.getValue(), DriftOrderStatus.CONFIRMED.getValue(), DriftOrderStatus.DELIVERED.getValue(),DriftOrderStatus.BACK.getValue(),DriftOrderStatus.FINISHED.getValue()};
        condition.put("consumerId",consumerId);
        condition.put("blockFlag",false);
        condition.put("statusList",statusList);
        response = orderService.fetchDriftOrder(condition);
        if(response.getResponseCode()==ResponseCode.RESPONSE_OK){
            for(int i=0;i<((List<DriftOrder>) response.getData()).size();i++){
                long day1 = expected.getTime();
                long day2 = ((List<DriftOrder>) response.getData()).get(i).getExpectedDate().getTime();
                if(-1296000000<day1-day2&&day1-day2<1296000000){
                    description = "该用户15天内已预约使用";
                    break;
                }
            }
        }

        DriftOrder driftOrder = new DriftOrder(consumerId, equipId, consignee, phone, address, province, city, district, description, activityId, expected, intervalDate);
        driftOrder.setTotalPrice(price);
        driftOrder.setRealPay(price);
        driftOrder.setList(list);
        response = orderService.createDriftOrder(driftOrder);
        if (response.getResponseCode() == ResponseCode.RESPONSE_ERROR) {
            result.setResponseCode(ResponseCode.RESPONSE_ERROR);
            result.setDescription("服务器正忙，请稍后再试");
        } else if (response.getResponseCode() == ResponseCode.RESPONSE_NULL) {
            result.setResponseCode(ResponseCode.RESPONSE_NULL);
            result.setDescription("无相关数据，请仔细检查");
        } else {
            result.setResponseCode(ResponseCode.RESPONSE_OK);
//            if ((int) (driftOrder.getRealPay() * 100) == 0) {
//                //todo 若订单金额为0，则自动更新订单状态为已付款
//            } else {
//                new Thread(() -> paymentService.createPay(driftOrder.getOrderId(), consumerId, (int) (driftOrder.getRealPay() * 100), activityName, ip)).start();
//            }
            result.setData(response.getData());
        }
        return result;
    }

    private boolean available(String activityId, Date date) {
        Map<String, Object> condition = new HashMap<>();
        condition.put("activityId", activityId);
        condition.put("blockFlag", false);
        ResultData response = activityService.fetchActivity(condition);
        if (response.getResponseCode() != ResponseCode.RESPONSE_OK) {
            return false;
        }
        Activity activity = ((List<Activity>) response.getData()).get(0);
        //判断传入的日期是否可以预约
        if (date.compareTo(activity.getStartTime()) < 0 || date.compareTo(activity.getEndTime()) > 0) {
            return false;
        }
        Calendar current = Calendar.getInstance();
        if (date.compareTo(current.getTime()) < 0) return false;

        //获取当天的预约情况
        condition.clear();
        condition.put("activityId", activityId);
        List statusList = new ArrayList();
        statusList.add(DriftOrderStatus.PAYED.getValue());
        statusList.add(DriftOrderStatus.CONFIRMED.getValue());
        statusList.add(DriftOrderStatus.DELIVERED.getValue());
        statusList.add(DriftOrderStatus.FINISHED.getValue());
        condition.put("statusList", statusList);
        condition.put("createDate", date);
        response = orderService.fetchDriftOrder(condition);
        if (response.getResponseCode() == ResponseCode.RESPONSE_NULL) return true;
        if (response.getResponseCode() == ResponseCode.RESPONSE_OK) {
            int size = ((List<Activity>) response.getData()).size();
            if (size < activity.getRepositorySize() * activity.getThreshold()) return true;
        }
        return false;
    }

    @PostMapping(value = "/pay/confirm")
    public ResultData confirm(String orderId, String code) {
        ResultData result = new ResultData();
        if (StringUtils.isEmpty(orderId)) {
            result.setResponseCode(ResponseCode.RESPONSE_ERROR);
            result.setDescription("请确认输入必要字段");
            return result;
        }
        ResultData response = paymentService.getTrade(orderId);
        if (response.getResponseCode() == ResponseCode.RESPONSE_OK) {
            result.setResponseCode(ResponseCode.RESPONSE_ERROR);
            result.setDescription("该订单已在交易中，无法继续使用优惠码，若需要使用优惠码，请重新创建订单");
            return result;
        }
        Map<String, Object> condition = new HashMap<>();
        condition.put("blockFlag", false);
        condition.put("orderId", orderId);
        response = orderService.fetchDriftOrder(condition);
        if (response.getResponseCode() != ResponseCode.RESPONSE_OK) {
            result.setResponseCode(ResponseCode.RESPONSE_ERROR);
            result.setDescription("无此订单内容，请确认无误后重试");
            return result;
        }
        DriftOrder order = ((List<DriftOrder>) response.getData()).get(0);
        String activityId = order.getActivityId();
        if (!StringUtils.isEmpty(order.getExcode())) {
            result.setResponseCode(ResponseCode.RESPONSE_ERROR);
            result.setDescription("该订单已使用过优惠码");
            return result;
        }
        if (!StringUtils.isEmpty(code)) {
            //判断码的长度，13位即为二维码，6位即为优惠码
            if (code.length() > 6) {
                response = machineService.checkQrcode(code);
                if (response.getResponseCode() != ResponseCode.RESPONSE_OK) {
                    result.setResponseCode(ResponseCode.RESPONSE_ERROR);
                    result.setDescription("二维码有误，请确认后重试");
                    return result;
                }
                condition.remove("orderId");
                condition.put("qrcode", code);
                response = qrExCodeService.fetchQrExCode(condition);
                if (response.getResponseCode() == ResponseCode.RESPONSE_OK) {
                    result.setResponseCode(ResponseCode.RESPONSE_ERROR);
                    result.setDescription("二维码已兑换过，不能重复使用");
                    return result;
                }
                if (response.getResponseCode() == ResponseCode.RESPONSE_ERROR) {
                    result.setResponseCode(ResponseCode.RESPONSE_ERROR);
                    result.setDescription("服务器正忙，请稍后重试");
                    return result;
                }
                condition.remove("qrcode");
                condition.put("activityId", activityId);
                condition.put("status", EXCodeStatus.CREATED.getValue());
                response = exCodeService.fetchEXCode(condition);
                if (response.getResponseCode() != ResponseCode.RESPONSE_OK) {
                    result.setResponseCode(ResponseCode.RESPONSE_ERROR);
                    result.setDescription("服务器正忙，请稍后重试");
                    return result;
                }
                EXCode exCode = ((List<EXCode>) response.getData()).get(0);
                //实现优惠码价格抵消
                order.setExcode(exCode.getCodeValue());
                order.setRealPay(Math.round(order.getTotalPrice() * 100 - exCode.getPrice() * 100) / 100.0);
                new Thread(() -> {
                    ResultData rd = updateExcode(code, exCode);
                }).start();
            } else if (code.length() == 6) {
                condition.remove("orderId");
                condition.put("activityId", activityId);
                condition.put("status", EXCodeStatus.EXCHANGED.getValue());
                condition.put("codeValue", code);
                response = exCodeService.fetchEXCode(condition);
                if (response.getResponseCode() != ResponseCode.RESPONSE_OK) {
                    result.setResponseCode(ResponseCode.RESPONSE_ERROR);
                    result.setDescription("输入的兑换码有误，请确认后重试");
                    return result;
                }
                EXCode exCode = ((List<EXCode>) response.getData()).get(0);
                //实现优惠码价格抵消
                order.setExcode(code);
                order.setRealPay(Math.round(order.getTotalPrice() * 100 - exCode.getPrice() * 100) / 100.0);
                new Thread(() -> {
                    condition.clear();
                    condition.put("codeId", exCode.getCodeId());
                    condition.put("status", EXCodeStatus.OCCUPIED.getValue());
                    exCodeService.modifyEXCode(condition);
                }).start();
            }
        }
        response = orderService.updateDriftOrder(order);
        if (response.getResponseCode() != ResponseCode.RESPONSE_OK) {
            result.setResponseCode(ResponseCode.RESPONSE_ERROR);
            result.setDescription("服务器正忙，请稍后重试");
        } else {
            result.setResponseCode(ResponseCode.RESPONSE_OK);
            result.setData(response.getData());
        }
        return result;
    }

    //更新兑换码状态
    private ResultData updateExcode(String qrcode, EXCode exCode) {
        ResultData result = new ResultData();
        QR_EXcode qr_eXcode = new QR_EXcode(qrcode, exCode.getCodeValue());
        ResultData response = qrExCodeService.createQrExCode(qr_eXcode);
        if (response.getResponseCode() != ResponseCode.RESPONSE_OK) {
            result.setResponseCode(ResponseCode.RESPONSE_ERROR);
            return result;
        }
        Map<String, Object> condition = new HashMap<>();
        condition.clear();
        condition.put("codeId", exCode.getCodeId());
        condition.put("status", EXCodeStatus.OCCUPIED.getValue());
        response = exCodeService.modifyEXCode(condition);
        if (response.getResponseCode() != ResponseCode.RESPONSE_OK) {
            result.setResponseCode(ResponseCode.RESPONSE_ERROR);
            return result;
        }
        result.setResponseCode(ResponseCode.RESPONSE_OK);
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
        int[] statusList = new int[]{DriftOrderStatus.APPLIED.getValue(), DriftOrderStatus.PAYED.getValue(), DriftOrderStatus.CONFIRMED.getValue(), DriftOrderStatus.DELIVERED.getValue(),DriftOrderStatus.BACK.getValue(),DriftOrderStatus.FINISHED.getValue()};
        Map<String, Object> condition = new HashMap<>();
        condition.put("consumerId", openid);
        condition.put("blockFlag", false);
        condition.put("statusList", statusList);
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
     * @param expressNo
     * @param expressFlag 0-->delivered ;1-->back ;other value is wrong
     * @param company
     * @return
     */
    @PostMapping(value = "/express/create")
    public ResultData createOrderExpress(String orderId, String expressNo, int expressFlag, String company) {
        ResultData result = new ResultData();
        if (StringUtil.isEmpty(orderId, expressNo, company)) {
            result.setResponseCode(ResponseCode.RESPONSE_ERROR);
            result.setDescription("Please make sure you fill all the required fields");
            return result;
        }
        Map<String, Object> condition = new HashMap<>();
        condition.put("orderId", orderId);
        condition.put("blockFlag", false);
        ResultData response1 = orderService.fetchDriftOrder(condition);
        if (response1.getResponseCode() != ResponseCode.RESPONSE_OK) {
            result.setResponseCode(ResponseCode.RESPONSE_ERROR);
            result.setDescription("未能查询到与".concat(orderId).concat("相关的订单"));
            return result;
        }

        //update order status
        DriftExpress driftExpress = new DriftExpress("", orderId, company, expressNo);
        driftExpress.setStatus(DriftExpressStatus.valueOf(expressFlag));
        if (driftExpress.getStatus() == null) {
            result.setResponseCode(ResponseCode.RESPONSE_ERROR);
            result.setDescription("订单标识异常");
            return result;
        } else {
            DriftOrder order = ((List<DriftOrder>) response1.getData()).get(0);
            if (driftExpress.getStatus() == DriftExpressStatus.DELIVERED)
                order.setStatus(DriftOrderStatus.DELIVERED);
            else if (driftExpress.getStatus() == DriftExpressStatus.BACk)
                order.setStatus(DriftOrderStatus.BACK);
            response1 = orderService.updateDriftOrder(order);
            if (response1.getResponseCode() != ResponseCode.RESPONSE_OK) {
                result.setResponseCode(ResponseCode.RESPONSE_ERROR);
                result.setDescription(new StringBuffer("Fail to update drift order with: ").append(order.toString()).toString());
                return result;
            }
        }

        //create driftExpress
        ResultData response2 = expressService.createExpress(driftExpress);
        if (response2.getResponseCode() == ResponseCode.RESPONSE_ERROR) {
            result.setResponseCode(ResponseCode.RESPONSE_ERROR);
            result.setDescription("Fail to store driftExpress message to database");
            return result;
        }
        if (response2.getResponseCode() == ResponseCode.RESPONSE_OK) {
            //快递100订阅此物流单
            new Thread(() -> {
                expressAgentService.subscribe(company, expressNo);
            }).start();
            result.setResponseCode(ResponseCode.RESPONSE_OK);
            result.setData(response2.getData());
        }
        return result;
    }

    /**
     * 根据orderId查询快递信息
     *
     * @param orderId
     * @return
     */
    @GetMapping(value = "/express/list")
    public ResultData getExpress(String orderId, int status) {
        ResultData result = new ResultData();
        if (StringUtils.isEmpty(orderId)) {
            result.setResponseCode(ResponseCode.RESPONSE_ERROR);
            result.setDescription("请输入orderId");
            return result;
        }
        Map<String, Object> condition = new HashMap<>();
        condition.put("blockFlag", false);
        condition.put("orderId", orderId);
        if (StringUtils.isEmpty(status)) {
            condition.put("status", status);
        }
        ResultData response = expressService.fetchExpress(condition);
        if(response.getResponseCode()==ResponseCode.RESPONSE_OK){
            result.setResponseCode(ResponseCode.RESPONSE_OK);
            result.setDescription("查询成功");
            String expressNo = ((List<Express>)response.getData()).get(0).getExpressNo();
            String expressCompany = ((List<Express>)response.getData()).get(0).getCompany();
            response = expressAgentService.getExpress(expressNo, expressCompany);
            if(response.getResponseCode()!=ResponseCode.RESPONSE_OK){
                result.setResponseCode(ResponseCode.RESPONSE_ERROR);
                result.setDescription("查询物流信息失败");
            }
            result.setData(response.getData());
        }else if(response.getResponseCode()==ResponseCode.RESPONSE_NULL){
            result.setResponseCode(ResponseCode.RESPONSE_NULL);
            result.setDescription("未找到相关记录");
        } else {
            result.setResponseCode(ResponseCode.RESPONSE_ERROR);
            result.setDescription("查询失败");
        }
        return result;
    }

    /**
     * 根据快递单号、快递公司号查询快递信息
     *
     * @param expressNo
     * @param expressCompany
     * @return
     */
    @GetMapping("/express/information")
    public ResultData obtainExpressInformation(String expressNo, String expressCompany) {
        ResultData result = new ResultData();
        if (StringUtil.isEmpty(expressNo, expressCompany)) {
            result.setResponseCode(ResponseCode.RESPONSE_ERROR);
            result.setDescription("Please ensure you fill all the required fields");
            return result;
        }
        ResultData response = expressAgentService.getExpress(expressNo, expressCompany);
        if (response.getResponseCode() == ResponseCode.RESPONSE_NULL) {
            result.setResponseCode(ResponseCode.RESPONSE_NULL);
            result.setDescription("未获取到相关的快递信息:" + expressNo);
        }
        if (response.getResponseCode() == ResponseCode.RESPONSE_ERROR) {
            result.setResponseCode(ResponseCode.RESPONSE_ERROR);
            result.setDescription("服务器繁忙，请稍后重试");
        }
        if (response.getResponseCode() == ResponseCode.RESPONSE_OK) {
            result.setResponseCode(ResponseCode.RESPONSE_OK);
            result.setData(response.getData());
        }
        return result;
    }

    @PostMapping(value = "/cancel")
    public ResultData orderCancel(@RequestParam("orderId") String orderId) {
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

        //取消优惠码使用
//        String excode = order.getExcode();
////        condition.clear();
////        condition.put("excode",excode);
////        condition.put("blockFlag",false);
////        response = qrExCodeService.fetchQrExCode(condition);
////        if(response.getResponseCode()==ResponseCode.RESPONSE_OK){
////            result.setResponseCode(ResponseCode.RESPONSE_OK);
////            condition.clear();
////            condition.put("excode",excode);
////            condition.put("blockFlag",true);
////            response = qrExCodeService.updateQrExCode(condition);
////            if(response.getResponseCode()!=ResponseCode.RESPONSE_OK){
////                result.setResponseCode(ResponseCode.RESPONSE_ERROR);
////                result.setDescription("恢复优惠券失败");
////                return result;
////            }
////        }else if(response.getResponseCode()==ResponseCode.RESPONSE_NULL){
////            condition.clear();
////            condition.put("codeValue",excode);
////            condition.put("status",EXCodeStatus.EXCHANGED);
////            response = exCodeService.modifyEXCode(condition);
////            if(response.getResponseCode()!=ResponseCode.RESPONSE_OK){
////                result.setResponseCode(ResponseCode.RESPONSE_ERROR);
////                result.setDescription("恢复优惠券失败");
////                return result;
////            }
////        }else {
////            result.setResponseCode(ResponseCode.RESPONSE_ERROR);
////            result.setDescription("服务器错误");
////            return result;
////        }
        order.setStatus(DriftOrderStatus.CANCELED);
        response = orderService.updateDriftOrder(order);
        if (response.getResponseCode() != ResponseCode.RESPONSE_OK) {
            result.setResponseCode(ResponseCode.RESPONSE_ERROR);
            result.setDescription(new StringBuffer("Fail to cancel drift order with: ").append(order.toString()).toString());
            return result;
        }
        result.setResponseCode(ResponseCode.RESPONSE_OK);
        result.setDescription("Drift order is already cancel");
        return result;
    }

    //将订单绑定机器码
    @PostMapping("/machinecode/submit")
    ResultData submitMachineCode(String orderId,String machineCode){
        ResultData result = new ResultData();
        if(org.apache.commons.lang.StringUtils.isEmpty(orderId)|| org.apache.commons.lang.StringUtils.isEmpty(machineCode)){
            result.setResponseCode(ResponseCode.RESPONSE_ERROR);
            result.setDescription("请提供正确的参数");
            return result;
        }
        Map<String, Object> condition = new HashMap<>();
        condition.put("orderId",orderId);
        condition.put("blockFlag",false);
        ResultData response = orderService.fetchDriftOrder(condition);
        if(response.getResponseCode()!=ResponseCode.RESPONSE_OK){
            result.setResponseCode(ResponseCode.RESPONSE_ERROR);
            result.setDescription("订单查找失败");
            return result;
        }
        result.setResponseCode(response.getResponseCode());
        result.setData(response.getData());
        DriftOrder order = ((List<DriftOrder>)response.getData()).get(0);
        order.setMachineOrderNo(machineCode);
        response = orderService.updateDriftOrder(order);
        if(response.getResponseCode()!=ResponseCode.RESPONSE_OK){
            result.setResponseCode(ResponseCode.RESPONSE_ERROR);
            result.setDescription("更新失败");
            return result;
        }
        result.setResponseCode(response.getResponseCode());
        result.setData(response.getData());
        return result;
    }

    @PostMapping("/express/receive")
    void receive(HttpServletRequest request, HttpServletResponse response){
        expressAgentService.receive(request,response);
    }
}
