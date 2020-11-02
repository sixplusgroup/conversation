package finley.gmair.controller;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import finley.gmair.form.drift.DriftOrderForm;
import finley.gmair.model.admin.Admin;
import finley.gmair.model.drift.*;
import finley.gmair.model.ordernew.TradeFrom;
import finley.gmair.model.wechat.OfficialAccountMessage;
import finley.gmair.service.*;
import finley.gmair.util.IPUtil;
import finley.gmair.util.ResponseCode;
import finley.gmair.util.ResultData;
import finley.gmair.util.StringUtil;
import finley.gmair.vo.drift.ActivityEquipmentVo;
import org.apache.commons.lang.time.DateUtils;
import org.apache.ibatis.session.RowBounds;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.*;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.*;

@RestController
@RequestMapping("/drift/order")
public class OrderController {
    private Logger logger = LoggerFactory.getLogger(OrderController.class);

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

    @Autowired
    private AuthService authService;

    @Autowired
    private DriftOrderCancelService driftOrderCancelService;

    @Autowired
    private DriftOrderActionService driftOrderActionService;

    @Autowired
    private WechatService wechatService;

    @Autowired
    private AuthConsumerService authConsumerService;

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
        if (!available(activityId, expected)) {
            result.setResponseCode(ResponseCode.RESPONSE_ERROR);
            result.setDescription("该日期仪器已被预定完，请重新选择日期");
            return result;
        }

        //构建订单中的子订单项
        List<DriftOrderItem> list = new ArrayList<>();
        DriftOrderItem equipItem = new DriftOrderItem();

        equipItem.setItemName(equipment.getEquipName());
        equipItem.setText(equipment.getText());
        equipItem.setUrl(equipment.getUrl());
        equipItem.setItemPrice(equipment.getEquipPrice());
        equipItem.setSingleNum(1);
        equipItem.setQuantity(1);
        equipItem.setTotalPrice(equipment.getEquipPrice() * 1);
        equipItem.setExQuantity(1);
        equipItem.setRealPrice(equipment.getEquipPrice() * 1);
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
                attachItem.setText(attachment.getText());
                attachItem.setUrl(attachment.getUrl());
                attachItem.setItemPrice(attachment.getAttachPrice());
                attachItem.setSingleNum(attachment.getAttachSingle());
                int num = ((Integer) e.getValue()).intValue();
                attachItem.setRealPrice(attachment.getAttachPrice() * num);
                attachItem.setExQuantity(num);
                num = num + num / 5;
                attachItem.setTotalPrice(attachment.getAttachPrice() * num);

                attachItem.setQuantity(num);
                list.add(attachItem);
            }
        }

        double price = 0;
        // 计算该订单的总价格
        for (DriftOrderItem orderItem : list) {
            int quantity = orderItem.getQuantity();
            quantity = quantity - (quantity - 1) / 5;
            price += (orderItem.getItemPrice()) * quantity;
        }
        //查询同一消费者的订单，检查预计使用日期在15天内添加提示
        condition.clear();
        int[] statusList = new int[]{DriftOrderStatus.PAYED.getValue(), DriftOrderStatus.CONFIRMED.getValue(), DriftOrderStatus.DELIVERED.getValue(), DriftOrderStatus.BACK.getValue(), DriftOrderStatus.FINISHED.getValue()};
        condition.put("consumerId", consumerId);
        condition.put("blockFlag", false);
        condition.put("statusList", statusList);
        response = orderService.fetchDriftOrder(condition);
        if (response.getResponseCode() == ResponseCode.RESPONSE_OK) {
            for (int i = 0; i < ((List<DriftOrder>) response.getData()).size(); i++) {
                long day1 = expected.getTime();
                long day2 = ((List<DriftOrder>) response.getData()).get(i).getExpectedDate().getTime();
                if (-1296000000 < day1 - day2 && day1 - day2 < 1296000000) {
                    description = "该用户15天内已预约使用";
                    break;
                }
            }
        }

        DriftOrder driftOrder = new DriftOrder(consumerId, equipId, consignee, phone, address, province, city, district, description, activityId, expected, intervalDate, TradeFrom.WECHAT);
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
        String orderId = driftOrder.getOrderId();
        String message = consignee + "创建了该订单,期望使用日期为：" + new SimpleDateFormat("yyyy-MM-dd").format(driftOrder.getExpectedDate());
        driftOrderActionService.create(new DriftOrderAction(orderId, message, consumerId));
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
        condition.put("blockFlag", false);
        response = orderService.fetchDriftOrder(condition);
        if (response.getResponseCode() == ResponseCode.RESPONSE_NULL) return true;
        if (response.getResponseCode() == ResponseCode.RESPONSE_OK) {
            int size = ((List<Activity>) response.getData()).size();
            if (size < activity.getRepositorySize() * activity.getThreshold()) return true;
        }
        return false;
    }

    /**
     * 提交优惠码
     *
     * @param orderId
     * @param code
     * @return
     */
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
            if (code.length() > 12) {
                //暂时关闭扫机器码功能
                result.setResponseCode(ResponseCode.RESPONSE_ERROR);
                result.setDescription("机器二维码兑换功能暂未开通");
                return result;

//                response = machineService.checkQrcode(code);
//                if (response.getResponseCode() != ResponseCode.RESPONSE_OK) {
//                    result.setResponseCode(ResponseCode.RESPONSE_ERROR);
//                    result.setDescription("二维码有误，请确认后重试");
//                    return result;
//                }
//                condition.remove("orderId");
//                condition.put("qrcode", code);
//                response = qrExCodeService.fetchQrExCode(condition);
//                if (response.getResponseCode() == ResponseCode.RESPONSE_OK) {
//                    result.setResponseCode(ResponseCode.RESPONSE_ERROR);
//                    result.setDescription("二维码已兑换过，不能重复使用");
//                    return result;
//                }
//                if (response.getResponseCode() == ResponseCode.RESPONSE_ERROR) {
//                    result.setResponseCode(ResponseCode.RESPONSE_ERROR);
//                    result.setDescription("服务器正忙，请稍后重试");
//                    return result;
//                }
//                condition.remove("qrcode");
//                condition.put("activityId", activityId);
//                condition.put("status", EXCodeStatus.CREATED.getValue());
//                response = exCodeService.fetchEXCode(condition);
//                if (response.getResponseCode() != ResponseCode.RESPONSE_OK) {
//                    result.setResponseCode(ResponseCode.RESPONSE_ERROR);
//                    result.setDescription("服务器正忙，请稍后重试");
//                    return result;
//                }
//                EXCode exCode = ((List<EXCode>) response.getData()).get(0);
//                //实现优惠码价格抵消
//                order.setExcode(exCode.getCodeValue());
//                order.setRealPay(Math.round(order.getTotalPrice() * 100 - exCode.getPrice() * 100) / 100.0);
//                new Thread(() -> {
//                    ResultData rd = updateExcode(code, exCode);
//                }).start();
            } else {
                condition.remove("orderId");
                condition.put("activityId", activityId);
//                condition.put("status", EXCodeStatus.EXCHANGED.getValue());
                condition.put("codeValue", code);
                response = exCodeService.fetchEXCode(condition);
                if (response.getResponseCode() != ResponseCode.RESPONSE_OK) {
                    result.setResponseCode(ResponseCode.RESPONSE_ERROR);
                    result.setDescription("输入的兑换码有误，请确认后重试");
                    return result;
                }
                condition.put("status", EXCodeStatus.EXCHANGED.getValue());
                response = exCodeService.fetchEXCode(condition);
                if (response.getResponseCode() != ResponseCode.RESPONSE_OK) {
                    result.setResponseCode(ResponseCode.RESPONSE_ERROR);
                    result.setDescription("该优惠码已使用过，取消之前的订单方可继续使用该优惠码");
                    return result;
                }
                EXCode exCode = ((List<EXCode>) response.getData()).get(0);
                //实现优惠码价格抵消
                if (order.getTotalPrice() - exCode.getPrice() <= 0) {
                    result.setResponseCode(ResponseCode.RESPONSE_ERROR);
                    result.setDescription("当前优惠券金额大于订单总价，无法使用");
                    return result;
                }
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
        String message = order.getConsignee() + "使用了优惠码：" + order.getExcode();
        driftOrderActionService.create(new DriftOrderAction(orderId, message, order.getConsumerId()));
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
    public ResultData orderList(String startTime, String endTime, String status, String search, String type) {
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
                case "BACK":
                    condition.put("status", 4);
                    break;
                case "FINISHED":
                    condition.put("status", 5);
                    break;
                case "CLOSED":
                    condition.put("status", 6);
                    break;
                case "CANCELED":
                    condition.put("status", 7);
                    break;
            }
        }
        if (!StringUtils.isEmpty(search)) {
            //删除对于订单状态的选择
//            condition.remove("status");
            String fuzzysearch = search.trim();
            if (type.equals("consignee")) {//如果内容为姓名
                condition.put("consignee", fuzzysearch);
            } else if (type.equals("orderId")) {//如果搜索内容为订单号
                condition.put("orderId", fuzzysearch);
            } else if (type.equals("machineOrderNo")) {//如果搜索内容为机器码
                condition.put("machineOrderNo", fuzzysearch);
            } else {//如果内容为手机号
                condition.put("phone", fuzzysearch);
            }

//            Pattern phone = Pattern.compile("^[1][3,4,5,7,8][0-9]{9}$");
//            Pattern id =  Pattern.compile("^GMO");
//            Pattern machine = Pattern.compile("^GMZNSK-");
//            Matcher m1 = phone.matcher(search);
//            Matcher m2 = id.matcher(search);
//            Matcher m3 = machine.matcher(search);
//            if (m1.find()) {//如果搜索内容为手机号
//                condition.put("phone", fuzzysearch);
//                System.out.println("手机号");
//            }
//            else if(m2.find()){//如果搜索内容为订单号
//                condition.put("orderId", fuzzysearch);
//                System.out.println("订单号");
//            }else if(m3.find()){//如果搜索内容为机器码
//                condition.put("machineOrderNo", fuzzysearch);
//                System.out.println("机器码");
//            }
//            else{
//                condition.put("consignee", fuzzysearch);
//                System.out.println("名字");
//            }
        }
        ResultData response = orderService.fetchDriftOrderPanel(condition);
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

    @GetMapping(value = "/orderById")
    public ResultData orderById(String orderId) {
        ResultData result = new ResultData();
        Map<String, Object> condition = new HashMap<>();
        condition.put("blockFlag", false);
        condition.put("orderId", orderId);
        ResultData response = orderService.fetchDriftOrderPanel(condition);
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
     * The method is called to get order information by useful conditions by page
     * contains time, province, city and so on
     *
     * @return
     */
    @GetMapping(value = "/listByPage")
    public ResultData orderListByPage(int curPage, int pageSize, String startTime, String endTime, String status, String search, String type) {
        ResultData result = new ResultData();
        Map<String, Object> condition = new HashMap<>();
        condition.put("blockFlag", false);
        if (!StringUtils.isEmpty(startTime)) {
            condition.put("startTime", startTime);
        }
        if (!StringUtils.isEmpty(endTime)) {
            condition.put("endTime", endTime);
        }
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
                case "BACK":
                    condition.put("status", 4);
                    break;
                case "FINISHED":
                    condition.put("status", 5);
                    break;
                case "CLOSED":
                    condition.put("status", 6);
                    break;
                case "CANCELED":
                    condition.put("status", 7);
                    break;
            }
        }
        if (!StringUtils.isEmpty(search)) {
            //删除对于订单状态的选择
//            condition.remove("status");
            String fuzzysearch = search.trim();
            if (type.equals("consignee")) {//如果内容为姓名
                condition.put("consignee", fuzzysearch);
            } else if (type.equals("orderId")) {//如果搜索内容为订单号
                condition.put("orderId", fuzzysearch);
            } else if (type.equals("machineOrderNo")) {//如果搜索内容为机器码
                condition.put("machineOrderNo", fuzzysearch);
            } else {//如果内容为手机号
                condition.put("phone", fuzzysearch);
            }

//            Pattern phone = Pattern.compile("^[1][3,4,5,7,8][0-9]{9}$");
//            Pattern id =  Pattern.compile("^GMO");
//            Pattern machine = Pattern.compile("^GMZNSK-");
//            Matcher m1 = phone.matcher(search);
//            Matcher m2 = id.matcher(search);
//            Matcher m3 = machine.matcher(search);
//            if (m1.find()) {//如果搜索内容为手机号
//                condition.put("phone", fuzzysearch);
//                System.out.println("手机号");
//            }
//            else if(m2.find()){//如果搜索内容为订单号
//                condition.put("orderId", fuzzysearch);
//                System.out.println("订单号");
//            }else if(m3.find()){//如果搜索内容为机器码
//                condition.put("machineOrderNo", fuzzysearch);
//                System.out.println("机器码");
//            }
//            else{
//                condition.put("consignee", fuzzysearch);
//                System.out.println("名字");
//            }
        }
        int size = (int) orderService.fetchDriftOrderSize(condition).getData();
        if (size == 0) {
            result.setResponseCode(ResponseCode.RESPONSE_NULL);
            result.setDescription("can not find order");
            return result;
        }
        RowBounds rowBounds = new RowBounds((curPage - 1) * pageSize, pageSize);
        ResultData response = orderService.fetchDriftOrderByPage(condition, rowBounds);
        if (response.getResponseCode() == ResponseCode.RESPONSE_ERROR) {
            result.setResponseCode(ResponseCode.RESPONSE_ERROR);
            result.setDescription("fail to fetch");
            return result;
        } else if (response.getResponseCode() == ResponseCode.RESPONSE_NULL) {
            result.setResponseCode(ResponseCode.RESPONSE_NULL);
            result.setDescription("not find any data according to your condition");
            return result;
        } else {
            JSONObject jsonObject = new JSONObject();
            jsonObject.put("orderList", response.getData());
            int totalPage = (size - 1) / pageSize + 1;
            jsonObject.put("totalPage", totalPage);
            result.setData(jsonObject);
            return result;
        }
//        List<DriftOrderPanel> resultList = (List<DriftOrderPanel>) response.getData();
//        int totalPage = resultList.size() / pageSize + 1;
//        if (curPage < 1 || curPage > totalPage) {
//            result.setResponseCode(ResponseCode.RESPONSE_ERROR);
//            result.setDescription("fail to got that page because that page not exist");
//            return result;
//        }
//        resultList = resultList.subList((curPage - 1) * pageSize, Math.min(curPage * pageSize, resultList.size()));
//        JSONObject jsonObject = new JSONObject();
//        jsonObject.put("totalPage", totalPage);
//        jsonObject.put("orderList", resultList);
//        result.setData(jsonObject);
//        result.setDescription("success to fetch data");
//        return result;
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
        int[] statusList = new int[]{DriftOrderStatus.PAYED.getValue(), DriftOrderStatus.CONFIRMED.getValue(), DriftOrderStatus.DELIVERED.getValue(), DriftOrderStatus.BACK.getValue(), DriftOrderStatus.FINISHED.getValue()};
        Map<String, Object> condition = new HashMap<>();
        condition.put("activityId", activityId);
        condition.put("blockFlag", false);
        condition.put("statusList", statusList);
        ResultData response = orderService.fetchDriftOrder(condition);
        if (response.getResponseCode() == ResponseCode.RESPONSE_NULL) {
            json.put("size", 0);
        }
        if (response.getResponseCode() == ResponseCode.RESPONSE_OK) {
            json.put("size", ((List) response.getData()).size() + 1000);
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
        int[] statusList = new int[]{DriftOrderStatus.APPLIED.getValue(), DriftOrderStatus.PAYED.getValue(), DriftOrderStatus.CONFIRMED.getValue(), DriftOrderStatus.DELIVERED.getValue(), DriftOrderStatus.BACK.getValue(), DriftOrderStatus.FINISHED.getValue()};
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
            //根据寄出还是寄回推送公众号消息
            if (driftExpress.getStatus() == DriftExpressStatus.DELIVERED) {
                deliveredMessage(orderId, expressNo, company);
            } else if (driftExpress.getStatus() == DriftExpressStatus.BACk)
                backedMessage(orderId);
        }

        //create driftExpress
        condition.clear();
        condition.put("blockFlag", false);
        condition.put("orderId", orderId);
        condition.put("status", expressFlag);
        ResultData response2 = expressService.fetchExpress(condition);
        System.out.println(response2.getData());
        if (response2.getResponseCode() == ResponseCode.RESPONSE_OK) {//express已存在则进行修改
            condition.clear();
            condition.put("expressId", ((List<DriftExpress>) response2.getData()).get(0).getExpressId());
            condition.put("expressNum", expressNo);
            condition.put("company", company);
            response2 = expressService.updateExpress(condition);
            if (response2.getResponseCode() != ResponseCode.RESPONSE_OK) {
                result.setResponseCode(response2.getResponseCode());
                result.setDescription(response2.getDescription());
                return result;
            }
        } else {//express不存在则创建
            response2 = expressService.createExpress(driftExpress);
            if (response2.getResponseCode() == ResponseCode.RESPONSE_ERROR) {
                result.setResponseCode(ResponseCode.RESPONSE_ERROR);
                result.setDescription("Fail to store driftExpress message to database");
                return result;
            }
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
        if (!StringUtils.isEmpty(status)) {
            condition.put("status", status);
        }
        ResultData response = expressService.fetchExpress(condition);
        if (response.getResponseCode() == ResponseCode.RESPONSE_OK) {
            result.setResponseCode(ResponseCode.RESPONSE_OK);
            result.setDescription("查询成功");
            String expressNo = ((List<DriftExpress>) response.getData()).get(0).getExpressNum();
            String expressCompany = ((List<DriftExpress>) response.getData()).get(0).getCompany();
            DriftExpress express = ((List<DriftExpress>) response.getData()).get(0);
            response = expressAgentService.getExpress(expressNo, expressCompany);
            if (response.getResponseCode() != ResponseCode.RESPONSE_OK) {
                result.setResponseCode(ResponseCode.RESPONSE_ERROR);
                result.setDescription("查询物流信息失败");
            }
            JSONObject json = new JSONObject();
            json.put("data", response.getData());
            json.put("express", express);
            result.setData(json);
        } else if (response.getResponseCode() == ResponseCode.RESPONSE_NULL) {
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
    public ResultData orderCancel(@RequestParam("orderId") String orderId, String account) {
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

        //取消订单表记录
        if (order.getStatus() == DriftOrderStatus.PAYED || order.getStatus() == DriftOrderStatus.CONFIRMED) {
            String openId = order.getConsumerId();
            double price = order.getRealPay();
            DriftOrderCancel driftOrderCancel = new DriftOrderCancel(orderId, openId, price);
            response = driftOrderCancelService.createCancel(driftOrderCancel);
            if (response.getResponseCode() != ResponseCode.RESPONSE_OK) {
                result.setResponseCode(ResponseCode.RESPONSE_ERROR);
                result.setDescription("取消订单表记录失败");
                return result;
            }
        }
        //取消优惠码使用
        if (order.getStatus() == DriftOrderStatus.APPLIED) {
            String excode = order.getExcode();
            condition.clear();
            condition.put("excode", excode);
            condition.put("blockFlag", false);
            response = qrExCodeService.fetchQrExCode(condition);
            if (response.getResponseCode() == ResponseCode.RESPONSE_OK) {
                result.setResponseCode(ResponseCode.RESPONSE_OK);
                condition.clear();
                condition.put("excode", excode);
                condition.put("blockFlag", true);
                response = qrExCodeService.updateQrExCode(condition);
                if (response.getResponseCode() != ResponseCode.RESPONSE_OK) {
                    result.setResponseCode(ResponseCode.RESPONSE_ERROR);
                    result.setDescription("恢复优惠券失败");
                    return result;
                }
            } else if (response.getResponseCode() == ResponseCode.RESPONSE_NULL) {
                condition.clear();
                condition.put("codeValue", excode);
                condition.put("blockFlag", false);
                response = exCodeService.fetchEXCode(condition);
                EXCode exCode = ((List<EXCode>) response.getData()).get(0);
                String codeId = exCode.getCodeId();
                condition.clear();
                condition.put("codeId", codeId);
                condition.put("status", EXCodeStatus.EXCHANGED.getValue());
                response = exCodeService.modifyEXCode(condition);
                if (response.getResponseCode() != ResponseCode.RESPONSE_OK) {
                    result.setResponseCode(ResponseCode.RESPONSE_ERROR);
                    result.setDescription("恢复优惠券失败");
                    return result;
                }
            } else {
                result.setResponseCode(ResponseCode.RESPONSE_ERROR);
                result.setDescription("服务器错误");
                return result;
            }
        }
        order.setStatus(DriftOrderStatus.CANCELED);
        response = orderService.updateDriftOrder(order);
        if (response.getResponseCode() != ResponseCode.RESPONSE_OK) {
            result.setResponseCode(ResponseCode.RESPONSE_ERROR);
            result.setDescription(new StringBuffer("Fail to cancel drift order with: ").append(order.toString()).toString());
            return result;
        }
        String message = "";
        String member = "";
        if (StringUtils.isEmpty(account)) {
            message = order.getConsignee() + "取消了该订单";
            member = order.getConsumerId();
        } else {
            Admin admin = JSONArray.parseArray(JSONObject.toJSONString(authService.getAdmin(account).getData()), Admin.class).get(0);
            member = admin.getAdminId();
            message = admin.getUsername() + "取消了该订单";
        }
        driftOrderActionService.create(new DriftOrderAction(order.getOrderId(), message, member));
        result.setResponseCode(ResponseCode.RESPONSE_OK);
        result.setDescription("Drift order is already cancel");
        return result;
    }

    //将订单绑定机器码
    @PostMapping("/machinecode/submit")
    ResultData submitMachineCode(String orderId, String machineCode) {
        ResultData result = new ResultData();
        if (org.apache.commons.lang.StringUtils.isEmpty(orderId) || org.apache.commons.lang.StringUtils.isEmpty(machineCode)) {
            result.setResponseCode(ResponseCode.RESPONSE_ERROR);
            result.setDescription("请提供正确的参数");
            return result;
        }
        Map<String, Object> condition = new HashMap<>();
        condition.put("orderId", orderId);
        condition.put("blockFlag", false);
        ResultData response = orderService.fetchDriftOrder(condition);
        if (response.getResponseCode() != ResponseCode.RESPONSE_OK) {
            result.setResponseCode(ResponseCode.RESPONSE_ERROR);
            result.setDescription("订单查找失败");
            return result;
        }
        result.setResponseCode(response.getResponseCode());
        result.setData(response.getData());
        DriftOrder order = ((List<DriftOrder>) response.getData()).get(0);
        order.setMachineOrderNo(machineCode);
        response = orderService.updateDriftOrder(order);
        if (response.getResponseCode() != ResponseCode.RESPONSE_OK) {
            result.setResponseCode(ResponseCode.RESPONSE_ERROR);
            result.setDescription("更新失败");
            return result;
        }
        result.setResponseCode(response.getResponseCode());
        result.setData(response.getData());
        return result;
    }

    @PostMapping("/express/receive")
    public void doReceive(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        JSONObject resJson = new JSONObject();
        try {
            String param = request.getParameter("param");
            ResultData result = expressAgentService.receive(param);
            if (result.getResponseCode() != ResponseCode.RESPONSE_OK) {
                resJson.put("result", false);
                resJson.put("returnCode", "500");
                resJson.put("message", "error");
                response.getWriter().print(resJson);
                return;
            }
            resJson.put("result", true);
            resJson.put("returnCode", "200");
            resJson.put("message", "success");
            response.getWriter().print(resJson);
        } catch (Exception e) {
            resJson.put("result", false);
            resJson.put("returnCode", "500");
            resJson.put("message", "error");
            e.printStackTrace();
            response.getWriter().print(resJson);
        }
    }

    /**
     * 更新订单信息
     *
     * @param orderId
     * @param consignee
     * @param phone
     * @param province
     * @param city
     * @param district
     * @param address
     * @param status
     * @return
     */
    @PostMapping("/update")
    ResultData updateOrder(String orderId, String consignee, String phone, String province, String city, String district, String address, String status, String expectedDate) {
        ResultData result = new ResultData();
        if (StringUtils.isEmpty(orderId)) {
            result.setResponseCode(ResponseCode.RESPONSE_ERROR);
            result.setDescription("请提供orderId");
            return result;
        }
        Map<String, Object> condition = new HashMap<>();
        condition.put("orderId", orderId);
        condition.put("blockFlag", false);
        ResultData response = orderService.fetchDriftOrder(condition);
        if (response.getResponseCode() != ResponseCode.RESPONSE_OK) {
            result.setResponseCode(ResponseCode.RESPONSE_ERROR);
            result.setDescription("订单查找失败");
            return result;
        }
        result.setResponseCode(response.getResponseCode());
        result.setData(response.getData());
        DriftOrder driftOrder = ((List<DriftOrder>) response.getData()).get(0);
        if (!StringUtils.isEmpty(consignee)) {
            driftOrder.setConsignee(consignee);
        }
        if (!StringUtils.isEmpty(phone)) {
            driftOrder.setPhone(phone);
        }
        if (!StringUtils.isEmpty(province)) {
            driftOrder.setProvince(province);
        }
        if (!StringUtils.isEmpty(city)) {
            driftOrder.setCity(city);
        }
        if (!StringUtils.isEmpty(district)) {
            driftOrder.setDistrict(district);
        }
        if (!StringUtils.isEmpty(address)) {
            driftOrder.setAddress(address);
        }
        if (!StringUtils.isEmpty(status)) {
            driftOrder.setStatus(DriftOrderStatus.valueOf(status));
            System.out.println(status);
        }
        if (!StringUtils.isEmpty(expectedDate) && !expectedDate.equals("undefined")) {
            SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
            Date expected = null;
            try {
                expected = sdf.parse(expectedDate);
            } catch (ParseException e) {
                e.printStackTrace();
            }
            if (!DateUtils.isSameDay(expected, driftOrder.getExpectedDate())) {
                //检查当天是否可以继续借出设备
                if (!available(driftOrder.getActivityId(), expected)) {
                    result.setResponseCode(ResponseCode.RESPONSE_ERROR);
                    result.setDescription("该日期仪器无法预约使用，请重新选择日期");
                    return result;
                } else {
                    driftOrder.setExpectedDate(expected);
                }
            }
        }
        response = orderService.updateDriftOrder(driftOrder);
        if (response.getResponseCode() != ResponseCode.RESPONSE_OK) {
            result.setResponseCode(ResponseCode.RESPONSE_ERROR);
            result.setDescription("更新失败");
            return result;
        }
//        switch (status) {
//            case "2":
//                confirmedMessage(orderId);
//                break;
//            case "3":
//                deliveredMessage(orderId);
//                break;
//            case "4":
//                backedMessage(orderId);
//                break;
//        }
        result.setResponseCode(response.getResponseCode());
        result.setData(response.getData());
        return result;
    }

    /**
     * 根据上传excel表格更新对应order和express
     *
     * @param orderId
     * @param machineOrderNo
     * @param expressNum
     * @param company
     * @param description
     * @return
     */

    @PostMapping("/changeStatus")
    public ResultData changeStatus(String orderId, String machineOrderNo, String expressNum, String company, String description, String account) {
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
        //用于记录修改状态
        String[] modify = new String[4];
        for (int s = 0; s < 4; s++) {
            modify[s] = "";
        }

        if (order.getStatus().getValue() != 2) {//只有订单状态为已确认才可修改order和express
            result.setResponseCode(ResponseCode.RESPONSE_ERROR);
            result.setDescription(new StringBuffer("Can not update order without status 'CONFIRMED' ").append(orderId).toString());
            return result;
        }

        //判断order机器码字段是否需要改动
        if (order.getMachineOrderNo() == null) {
            if (!machineOrderNo.equals("")) {
                order.setMachineOrderNo(machineOrderNo);
                modify[0] = machineOrderNo;
            }
        } else if (!order.getMachineOrderNo().equals(machineOrderNo)) {
            order.setMachineOrderNo(machineOrderNo);
            modify[0] = machineOrderNo;
        }

        //判断order备注字段是否需要改动
        if (order.getDescription() == null) {
            if (!description.equals("")) {
                order.setDescription(description);
                modify[1] = description;
            }

        } else if (!order.getDescription().equals(description)) {
            order.setDescription(description);
            modify[1] = description;
        }

        response = orderService.updateDriftOrder(order);
//        Admin admin = JSONObject.parseObject(JSONObject.toJSONString(authService.getAdmin(account).getData()),Admin.class);
        Admin admin = JSONArray.parseArray(JSONObject.toJSONString(authService.getAdmin(account).getData()), Admin.class).get(0);
        if (response.getResponseCode() != ResponseCode.RESPONSE_OK) {
            result.setResponseCode(ResponseCode.RESPONSE_ERROR);
            result.setDescription(new StringBuffer("Fail to update drift order with: ").append(order.toString()).toString());
            if (!modify[0].equals("") || !modify[1].equals(""))
                createAction(orderId, StringUtil.toMessage(modify, expressNum, company, machineOrderNo, description, admin.getUsername()), admin.getAdminId());
            return result;
        }

        Map<String, Object> condition1 = new HashMap<>();

        condition1.put("blockFlag", false);
        condition1.put("orderId", orderId);
        ResultData response2 = expressService.fetchExpress(condition);

        if (response2.getResponseCode() == ResponseCode.RESPONSE_ERROR) {
            result.setResponseCode(ResponseCode.RESPONSE_ERROR);
            result.setDescription(new StringBuffer("Fail to retrieve express with orderId: ").append(orderId).toString());
            if (!modify[0].equals("") || !modify[1].equals(""))
                createAction(orderId, StringUtil.toMessage(modify, expressNum, company, machineOrderNo, description, admin.getUsername()), admin.getAdminId());
            return result;
        }
        if (response2.getResponseCode() == ResponseCode.RESPONSE_NULL) {//判断该订单对应的快递单是否存在，不存在则创建
            if (company == null && expressNum == null) {
                result.setResponseCode(ResponseCode.RESPONSE_OK);
                result.setDescription(new StringBuffer("The express do not need to change （null）").toString());
                if (!modify[0].equals("") || !modify[1].equals(""))
                    createAction(orderId, StringUtil.toMessage(modify, expressNum, company, machineOrderNo, description, admin.getUsername()), admin.getAdminId());
                return result;
            } else {
                createOrderExpress(orderId, expressNum, 0, company);
                modify[2] = expressNum;
                modify[3] = company;
                createAction(orderId, StringUtil.toMessage(modify, expressNum, company, machineOrderNo, description, admin.getUsername()), admin.getAdminId());
                result.setResponseCode(ResponseCode.RESPONSE_OK);
                result.setDescription(new StringBuffer("The express is created ").toString());
                return result;
            }
        }


        DriftExpress express = ((List<DriftExpress>) response2.getData()).get(0);

        if (!express.getExpressNum().equals(expressNum) || !express.getCompany().equals(company)) {//express存在时，判断express是否需要更改
            createOrderExpress(orderId, expressNum, 0, company);
            modify[2] = expressNum;
            modify[3] = company;
            result.setResponseCode(ResponseCode.RESPONSE_OK);
            result.setDescription(new StringBuffer("The express is updated ").toString());

            createAction(orderId, StringUtil.toMessage(modify, expressNum, company, machineOrderNo, description, admin.getUsername()), admin.getAdminId());
            return result;
        } else {
            result.setResponseCode(ResponseCode.RESPONSE_OK);
            result.setDescription(new StringBuffer("The express do not need to be update ").toString());
            if (!modify[0].equals("") || !modify[1].equals(""))
                createAction(orderId, StringUtil.toMessage(modify, expressNum, company, machineOrderNo, description, admin.getUsername()), admin.getAdminId());
            return result;
        }

    }

    /**
     * 查询订单操作日志
     *
     * @param actionId
     * @param orderId
     * @param member
     * @return
     */
    @GetMapping("/action/select")
    ResultData actionSelect(String actionId, String orderId, String member) {
        ResultData result = new ResultData();
        Map<String, Object> condition = new HashMap<>();
        if (StringUtils.isEmpty(actionId) && StringUtils.isEmpty(orderId) && StringUtils.isEmpty(member)) {
            result.setResponseCode(ResponseCode.RESPONSE_ERROR);
            result.setDescription("请提供查询条件");
            return result;
        }
        if (!StringUtils.isEmpty(actionId)) {
            condition.put("actionId", actionId);
        }
        if (!StringUtils.isEmpty(orderId)) {
            condition.put("orderId", orderId);
        }
        if (!StringUtils.isEmpty(member)) {
            condition.put("member", member);
        }
        condition.put("blockFlag", false);
        ResultData response = driftOrderActionService.fetch(condition);
        if (response.getResponseCode() == ResponseCode.RESPONSE_NULL) {
            result.setResponseCode(ResponseCode.RESPONSE_NULL);
            result.setDescription("订单日志为空");
            return result;
        } else if (response.getResponseCode() == ResponseCode.RESPONSE_ERROR) {
            result.setResponseCode(ResponseCode.RESPONSE_ERROR);
            result.setDescription("查询出现错误");
            return result;
        }
        result.setResponseCode(response.getResponseCode());
        result.setData(response.getData());
        return result;
    }

    /**
     * 创建订单操作日志
     *
     * @param orderId
     * @param message
     * @param member
     * @return
     */
    @PostMapping("/action/create")
    ResultData createAction(String orderId, String message, String member) {
        ResultData result = new ResultData();
        ResultData response = driftOrderActionService.create(new DriftOrderAction(orderId, message, member));
        if (response.getResponseCode() == ResponseCode.RESPONSE_ERROR) {
            result.setResponseCode(ResponseCode.RESPONSE_ERROR);
            result.setDescription("订单操作日志创建失败");
            System.out.println("失败");
            return result;
        }
        result.setResponseCode(response.getResponseCode());
        result.setData(response.getData());
        System.out.println(result.getDescription());
        return result;
    }

    /**
     * 获取订单取消记录用于退款
     *
     * @param status
     * @return
     */
    @GetMapping("/cancel/record/select")
    ResultData selectCancelRecord(String status) {
        ResultData result = new ResultData();
        if (StringUtils.isEmpty(status)) {
            result.setResponseCode(ResponseCode.RESPONSE_ERROR);
            result.setDescription("请提供查询条件");
            return result;
        }
        Map<String, Object> condition = new HashMap<>();
        if (status.equals("0")) {
            condition.put("isFinish", false);
        } else if (status.equals("1")) {
            condition.put("isFinish", true);
        }
        condition.put("blockFlag", false);
        ResultData response = driftOrderCancelService.fetchCancel(condition);
        if (response.getResponseCode() == ResponseCode.RESPONSE_ERROR) {
            result.setResponseCode(ResponseCode.RESPONSE_ERROR);
            result.setDescription("查询失败");
        } else if (response.getResponseCode() == ResponseCode.RESPONSE_NULL) {
            result.setResponseCode(ResponseCode.RESPONSE_NULL);
            result.setDescription("查询结果为空");
        } else {
            result.setResponseCode(ResponseCode.RESPONSE_OK);
            result.setData(response.getData());
        }
        return result;
    }

    /**
     * 根据orderId更新取消的订单为已完成
     *
     * @param orderId
     * @return
     */
    @PostMapping("/cancel/record/update")
    ResultData updateCancelRecord(String orderId) {
        ResultData result = new ResultData();
        if (StringUtils.isEmpty(orderId)) {
            result.setResponseCode(ResponseCode.RESPONSE_ERROR);
            result.setDescription("请提供orderId");
            return result;
        }
        Map<String, Object> condition = new HashMap<>();
        condition.put("orderId", orderId);
        condition.put("blockFlag", false);
        ResultData response = driftOrderCancelService.fetchCancel(condition);
        if (response.getResponseCode() == ResponseCode.RESPONSE_NULL) {
            result.setResponseCode(ResponseCode.RESPONSE_NULL);
            result.setDescription("未找到相关订单");
            return result;
        } else if (response.getResponseCode() == ResponseCode.RESPONSE_ERROR) {
            result.setResponseCode(ResponseCode.RESPONSE_ERROR);
            result.setDescription("查询出现错误");
            return result;
        }
        DriftOrderCancel driftOrderCancel = ((List<DriftOrderCancel>) response.getData()).get(0);
        driftOrderCancel.setFinish(true);
        response = driftOrderCancelService.updateCancel(driftOrderCancel);
        if (response.getResponseCode() != ResponseCode.RESPONSE_OK) {
            result.setResponseCode(ResponseCode.RESPONSE_ERROR);
            result.setDescription("更新失败");
            return result;
        }
        result.setData(response.getData());
        return result;
    }

    /**
     * 订单状态变为已确认时向用户推送消息
     *
     * @param orderId
     * @return
     */
    @GetMapping("/confirmedMessage")
    public ResultData confirmedMessage(String orderId) {
        ResultData resultData = new ResultData();
        //根据orderId获取手机号
        ResultData re = orderById(orderId);

        String phone = ((List<DriftOrderPanel>) re.getData()).get(0).getPhone();
        System.out.println(phone);
        //根据手机号获取wechat
        ResultData res = authConsumerService.probeWechatByPhone(phone);
        if (res.getResponseCode() == ResponseCode.RESPONSE_ERROR) {
            resultData.setResponseCode(ResponseCode.RESPONSE_ERROR);
            resultData.setDescription("查询wechat失败");
            return resultData;
        } else if (res.getResponseCode() == ResponseCode.RESPONSE_NULL) {
            resultData.setResponseCode(ResponseCode.RESPONSE_NULL);
            resultData.setDescription("该用户未注册或未绑定wechat");
            return resultData;
        }
        String wechat = res.getData().toString();
        resultData.setData(wechat);

        //调用wechat消息推送方法
//        ResultData result = wechatService.confirmMessage("12312");
        return resultData;
    }

    /**
     * 订单状态变为已发货时向用户推送消息
     *
     * @param orderId
     * @return
     */
    @GetMapping("/notify/delivered")
    public ResultData deliveredMessage(String orderId, String expressOutNum, String expressOutCompany) {
        ResultData resultData = new ResultData();
        //根据orderId获取手机号
        ResultData re = orderById(orderId);
        //todo 快递公司信息数据库存储
        if (expressOutCompany.equals("shunfeng")) {
            expressOutCompany = "顺丰快递";
        } else if (expressOutCompany.equals("yuantong")) {
            expressOutCompany = "圆通快递";
        }
        String phone = ((List<DriftOrderPanel>) re.getData()).get(0).getPhone();

        //根据手机号获取wechat
        ResultData res = authConsumerService.probeWechatByPhone(phone);
        if (res.getResponseCode() == ResponseCode.RESPONSE_ERROR) {
            resultData.setResponseCode(ResponseCode.RESPONSE_ERROR);
            resultData.setDescription("查询wechat失败");
            return resultData;
        } else if (res.getResponseCode() == ResponseCode.RESPONSE_NULL) {
            resultData.setResponseCode(ResponseCode.RESPONSE_NULL);
            resultData.setDescription("该用户未注册或未绑定wechat");
            return resultData;
        }
        String wechat = res.getData().toString();
        resultData.setData(wechat);

        //设置推送JSON参数
        JSONObject jsonObject = new JSONObject();
        jsonObject.put("touser", wechat);   // openid

        JSONObject data = new JSONObject();
        JSONObject first = new JSONObject();
        first.put("value", "您好，您租赁的甲醛检测设备已发货。请注意查收");
        first.put("color", "#173177");
        JSONObject keyword1 = new JSONObject();
        keyword1.put("value", expressOutNum);
        keyword1.put("color", "#173177");
        JSONObject keyword2 = new JSONObject();
        keyword2.put("value", expressOutCompany);
        keyword2.put("color", "#173177");
        JSONObject remark = new JSONObject();
        remark.put("value", "详情请查看果麦检测小程序");
        remark.put("color", "#173177");

        data.put("first", first);
        data.put("keyword1", keyword1);
        data.put("keyword2", keyword2);
        data.put("remark", remark);

        jsonObject.put("data", data);

        String json = jsonObject.toString();

        //调用wechat消息推送方法
        ResultData result = wechatService.sendMessage(OfficialAccountMessage.DELIVERED.getValue(), json, orderId);
        return resultData;
    }

    /**
     * 订单状态变为已寄回时向用户推送消息
     *
     * @param orderId
     * @return
     */
    @GetMapping("/notify/sendback")
    public ResultData backedMessage(String orderId) {
        ResultData resultData = new ResultData();

        return resultData;
    }

    /**
     * 提醒用户归还设备
     *
     * @param
     * @return
     */
    @GetMapping("/notify/return")
    public ResultData returnMessage() {
        logger.info("[Info]: processing notifications to be sent to those who are expected to return the equipment they reserved");
        ResultData result = new ResultData();
        //获取今天日期
        Date date = new Date();
        SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd");
        String dd = formatter.format(date);
        Map<String, Object> condition = new HashMap<>();
        condition.put("blockFlag", false);
        condition.put("today", formatter.format(date));
        condition.put("status", DriftOrderStatus.DELIVERED.getValue());
        ResultData response = orderService.fetchDriftOrderPanel(condition);
        if (response.getResponseCode() != ResponseCode.RESPONSE_OK) {
            result.setDescription("今日没有需要推送消息");
            result.setResponseCode(ResponseCode.RESPONSE_NULL);
            return result;
        }

        //向列表中所有用户推送消息
        for (DriftOrderPanel p : ((List<DriftOrderPanel>) response.getData())) {
            //根据当前日期查找今日过期设备，并获取手机号
            String phone = p.getPhone();
            //根据手机号查找用户wechat
            response = authConsumerService.probeWechatByPhone(phone);
            if (response.getResponseCode() == ResponseCode.RESPONSE_ERROR) {

                continue;
            } else if (response.getResponseCode() == ResponseCode.RESPONSE_NULL) {

                continue;
            }
            String wechat = response.getData().toString();

            JSONObject jsonObject = new JSONObject();
            jsonObject.put("touser", wechat);   // openid

            JSONObject data = new JSONObject();
            JSONObject first = new JSONObject();
            first.put("value", "您好，您租赁的甲醛检测设备将于今日到期。请按时寄回，感谢您的使用");
            first.put("color", "#173177");
            JSONObject keyword1 = new JSONObject();
            keyword1.put("value", p.getActivityName());
            keyword1.put("color", "#173177");
            JSONObject keyword2 = new JSONObject();
            keyword2.put("value", formatter.format(p.getExpectedDate()) + "至" + dd);
            keyword2.put("color", "#173177");
            JSONObject keyword3 = new JSONObject();
            keyword3.put("value", dd);
            keyword3.put("color", "#173177");
            JSONObject remark = new JSONObject();
            remark.put("value", "详情请查看果麦检测小程序");
            remark.put("color", "#173177");

            data.put("first", first);
            data.put("keyword1", keyword1);
            data.put("keyword2", keyword2);
            data.put("keyword3", keyword3);
            data.put("remark", remark);

            jsonObject.put("data", data);

            String json = jsonObject.toString();

            //公众号推送消息
            wechatService.sendMessage(OfficialAccountMessage.RETURN.getValue(), json, p.getOrderId());
        }
        return response;
    }

    /**
     * 订单中台同步订单
     *
     * @param orderExpress
     * @return
     */
    @PostMapping("/sync")
    public ResultData syncOrder(@RequestBody DriftOrderExpress orderExpress) {
        ResultData result = new ResultData();
        if (orderExpress == null) {
            result.setResponseCode(ResponseCode.RESPONSE_ERROR);
            result.setDescription("DriftOrderExpress object is null");
            return result;
        }
        if (orderExpress.getDriftOrder() == null || orderExpress.getDriftOrder().getOrderId() == null) {
            result.setResponseCode(ResponseCode.RESPONSE_ERROR);
            result.setDescription("DriftOrder object is null or orderId is null");
            return result;
        }
        DriftOrder order = orderExpress.getDriftOrder();
        DriftExpress express = orderExpress.getDriftExpress();
        logger.info("syncOrder, driftOrder:{}, driftExpress:{}", order, express);

        //填充默认活动id和设备id
        Map<String, Object> condition = new HashMap<>();
        condition.put("blockFlag", false);
        ResultData response = activityService.fetchActivityEquipment(condition);
        if (response.getResponseCode() != ResponseCode.RESPONSE_OK) {
            result.setResponseCode(ResponseCode.RESPONSE_ERROR);
            result.setDescription("failed to find activityId and equipId");
            return result;
        }
        ActivityEquipmentVo activityEquipmentVo = ((List<ActivityEquipmentVo>) response.getData()).get(0);
        order.setActivityId(activityEquipmentVo.getActivityId());
        order.setEquipId(activityEquipmentVo.getEquipmentId());
        condition.clear();
        condition.put("activityId", activityEquipmentVo.getActivityId());
        condition.put("blockFlag", false);
        response = activityService.fetchActivity(condition);
        if (response.getResponseCode() != ResponseCode.RESPONSE_OK) {
            result.setResponseCode(ResponseCode.RESPONSE_ERROR);
            result.setDescription("failed to find activity");
            return result;
        }
        //获取活动信息,填充intervalDate
        Activity activity = ((List<Activity>) response.getData()).get(0);
        order.setIntervalDate(activity.getReservableDays());


        condition.clear();
        condition.put("orderId", order.getOrderId());
        condition.put("blockFlag", false);
        ResultData fetchOrderResponse = orderService.fetchDriftOrder(condition);
        if (fetchOrderResponse.getResponseCode() == ResponseCode.RESPONSE_ERROR) {
            return fetchOrderResponse;
        }
        //orderId不存在,创建订单以及物流信息
        else if (fetchOrderResponse.getResponseCode() == ResponseCode.RESPONSE_NULL) {
            ResultData createResponse = orderService.createDriftOrderWithId(order);
            if (createResponse.getResponseCode() != ResponseCode.RESPONSE_OK) {
                return createResponse;
            }
            //如果订单状态为已发货或已完成，创建物流信息
            if (order.getStatus() == DriftOrderStatus.DELIVERED || order.getStatus() == DriftOrderStatus.FINISHED) {
                ResultData createExpressResponse = expressService.createExpress(express);
                if (createExpressResponse.getResponseCode() != ResponseCode.RESPONSE_OK) {
                    return createExpressResponse;
                }
            }
            //写入操作记录表
            String message = "一条订单被同步创建：订单来源:" + order.getTradeFrom().name() + ",订单状态:" + order.getStatus().name();
            DriftOrderAction action = new DriftOrderAction(order.getOrderId(), message, order.getTradeFrom().name());
            driftOrderActionService.create(action);
        }
        //orderId已存在，更新订单，创建或更新物流信息
        else if (fetchOrderResponse.getResponseCode() == ResponseCode.RESPONSE_OK) {
            ResultData updateResponse = orderService.updateDriftOrder(order);
            if (updateResponse.getResponseCode() != ResponseCode.RESPONSE_OK) {
                return updateResponse;
            }
            //如果订单状态为已发货或已完成，创建或更新物流信息
            if (order.getStatus() == DriftOrderStatus.DELIVERED || order.getStatus() == DriftOrderStatus.FINISHED) {
                condition.clear();
                condition.put("blockFlag", false);
                condition.put("orderId", express.getOrderId());
                ResultData fetchExpressResponse = expressService.fetchExpress(condition);
                if (fetchExpressResponse.getResponseCode() == ResponseCode.RESPONSE_ERROR) {
                    return fetchExpressResponse;
                } else if (fetchExpressResponse.getResponseCode() == ResponseCode.RESPONSE_NULL) {
                    //express不存在则创建
                    ResultData createExpressResponse = expressService.createExpress(express);
                    if (createExpressResponse.getResponseCode() != ResponseCode.RESPONSE_OK) {
                        return createExpressResponse;
                    }
                } else if (fetchExpressResponse.getResponseCode() == ResponseCode.RESPONSE_OK) {
                    //express已存在则进行修改
                    condition.clear();
                    condition.put("expressId", express.getExpressId());
                    condition.put("expressNum", express.getExpressNum());
                    condition.put("company", express.getCompany());
                    ResultData updateExpressResponse = expressService.updateExpress(condition);
                    if (updateExpressResponse.getResponseCode() != ResponseCode.RESPONSE_OK) {
                        logger.info("error4");
                        return updateExpressResponse;
                    }
                }
            }
            //写入操作记录表
            String message = "一条订单被同步更新：订单来源:" + order.getTradeFrom().name() + ",订单状态:" + order.getStatus().name();
            DriftOrderAction action = new DriftOrderAction(order.getOrderId(), message, order.getTradeFrom().name());
            driftOrderActionService.create(action);
        }
        return result;
    }
}
