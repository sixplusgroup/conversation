package finley.gmair.controller;


import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import finley.gmair.form.drift.ChangeOrderStatusForm;
import finley.gmair.form.installation.AssignForm;
import finley.gmair.model.admin.Admin;
import finley.gmair.model.drift.DriftOrderCancel;
import finley.gmair.model.drift.DriftOrderPanel;
import finley.gmair.model.drift.DriftOrderStatus;
import finley.gmair.model.installation.AssignReport;
import finley.gmair.service.AuthService;
import finley.gmair.service.DriftService;
import finley.gmair.util.DriftUtil;
import finley.gmair.util.ExcelUtil;
import finley.gmair.util.ResponseCode;
import finley.gmair.util.ResultData;
import org.apache.commons.lang.StringUtils;
import org.apache.poi.hssf.usermodel.HSSFCell;
import org.apache.poi.hssf.usermodel.HSSFRow;
import org.apache.poi.hssf.usermodel.HSSFSheet;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.ss.usermodel.WorkbookFactory;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.MediaType;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.multipart.MultipartHttpServletRequest;

import javax.print.DocFlavor;
import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletResponse;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.io.OutputStream;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Map;

@CrossOrigin
@RestController
@RequestMapping("/management/drift")
public class DriftController {
    private Logger logger = LoggerFactory.getLogger(DriftController.class);

    @Autowired
    private DriftService driftService;

    @Autowired
    private AuthService authService;

    @Value("${temp_path}")
    private String baseDir;

    @GetMapping("/order/list")
    ResultData driftOrderList(String startTime,String endTime,String status,String search,String type){
        return driftService.driftOrderList(startTime,endTime,status,search,type);
    }

    @GetMapping("/order/listByPage")
    ResultData driftOrderListByPage(int curPage , int pageSize , String startTime,String endTime,String status,String search,String type){
        return driftService.driftOrderListByPage(curPage,pageSize,startTime,endTime,status,search,type);
    }

    @GetMapping("/order/{orderId}")
    ResultData selectByOrderId(@PathVariable("orderId") String orderId){
        ResultData result = new ResultData();
        if(StringUtils.isEmpty(orderId)){
            result.setResponseCode(ResponseCode.RESPONSE_ERROR);
            result.setDescription("请提供orderId");
            return result;
        }
        result = driftService.selectByOrderId(orderId);
        return result;
    }

    @PostMapping("/order/express/submit")
    ResultData submitMachineCode(String orderId,String machineCode,String expressNo, int expressFlag, String company){
        ResultData result = new ResultData();
        if(StringUtils.isEmpty(orderId)||StringUtils.isEmpty(machineCode)||StringUtils.isEmpty(expressNo)||StringUtils.isEmpty(company)){
            result.setResponseCode(ResponseCode.RESPONSE_ERROR);
            result.setDescription("请提供正确的参数");
            return result;
        }
        ResultData response = driftService.updateMachineCode(orderId,machineCode);
        if(response.getResponseCode()!=ResponseCode.RESPONSE_OK){
            result.setResponseCode(response.getResponseCode());
            result.setDescription("更新机器码失败");
            return result;
        }
        response = driftService.createOrderExpress(orderId,expressNo,expressFlag,company);
        if(response.getResponseCode()!=ResponseCode.RESPONSE_OK){
            result.setResponseCode(response.getResponseCode());
            result.setDescription("物流信息创建失败");
            return result;
        }
        String account = (String) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
//        Admin admin = JSONObject.parseObject(JSONObject.toJSONString(authService.getAdmin(account).getData()),Admin.class);
        Admin admin = JSONArray.parseArray(JSONObject.toJSONString(authService.getAdmin(account).getData()),Admin.class).get(0);
        String message = admin.getUsername();
        if(expressFlag==0){
            message+="更新了寄出快递单号："+expressNo+"，快递公司："+company+"，机器码："+machineCode;
        }else {
            message+="更新了寄回快递单号："+expressNo+"，快递公司："+company+"，机器码："+machineCode;
        }
        driftService.createAction(orderId,message,admin.getAdminId());
        result.setResponseCode(response.getResponseCode());
        result.setData(response.getData());
        result.setDescription(response.getDescription());
        return result;
    }

    /**
     * 根据activityId查询活动详情
     * @param activityId
     * @return
     */
    @GetMapping("/activity/{activityId}/profile")
    ResultData getActivityDetail(@PathVariable("activityId") String activityId){
        return driftService.getActivityDetail(activityId);
    }

    /**
     * 取消订单
     * @param orderId
     * @return
     */
    @PostMapping("/order/cancel")
    ResultData cancelOrder(String orderId){
        ResultData result = new ResultData();
        if(StringUtils.isEmpty(orderId)){
            result.setResponseCode(ResponseCode.RESPONSE_ERROR);
            result.setDescription("请提供订单编号");
            return result;
        }
        String account = (String) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        result = driftService.cancelOrder(orderId,account);
        return result;
    }

    /**
     * 获取快递详情信息
     * @param orderId
     * @param status
     * @return
     */
    @GetMapping("/order/express/select")
    ResultData getExpressDetail(String orderId,int status){
        return driftService.getExpressDetail(orderId,status);
    }

    /**
     * 更新订单信息
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
    @PostMapping("/order/update")
    ResultData updateOrder(String orderId,String consignee,String phone,String province,String city,String district,String address,String status){
        ResultData result = new ResultData();
        if(StringUtils.isEmpty(orderId)){
            result.setResponseCode(ResponseCode.RESPONSE_ERROR);
            result.setDescription("请提供orderId");
            return result;
        }
        result = driftService.updateOrder(orderId,consignee,phone,province,city,district,address,status);
        String account = (String) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
//        Admin admin = JSONObject.parseObject(JSONObject.toJSONString(authService.getAdmin(account).getData()),Admin.class);
        Admin admin = JSONArray.parseArray(JSONObject.toJSONString(authService.getAdmin(account).getData()),Admin.class).get(0);
        String message = admin.getUsername()+"更新了订单："+DriftUtil.updateMessage(consignee,phone,province,city,district,address,status);
        driftService.createAction(orderId,message,admin.getAdminId());
        return result;
    }

    /**
     * 下载当前搜索到的订单
     * @param startTime
     * @param endTime
     * @param status
     * @param search
     * @param response
     * @param type
     * @return
     */
    @GetMapping("/order/download")
    String order_download(String startTime,String endTime,String status,String search,HttpServletResponse response,String type){
        ResultData result=new ResultData();
        result = driftService.driftOrderList(startTime,endTime,status,search,type);
        String fileName=new SimpleDateFormat("yyyy-MM-dd").format(new Date())+".xls";
        try {
            //查询数据库中所有的数据
            String data= JSONObject.toJSONString(result.getData());
            List<DriftOrderPanel> list= JSONArray.parseArray(data,DriftOrderPanel.class);
//            logger.info(result.getData().toString());
            HSSFWorkbook wb = new HSSFWorkbook();
            HSSFSheet sheet = wb.createSheet("sheet1");
            String[] n = {
                    "订单编号",
                    "活动名称",
                    "设备名称",
                    "消费者编号",
                    "姓名",
                    "联系方式",
                    "地址",
                    "试纸数量",
                    "试纸单价",
                    "试纸总价",
                    "试纸实际价格",
                    "使用日期",
                    "优惠码",
                    "订单状态",
                    "机器码",
                    "寄出快递单号",
                    "寄出快递公司",
                    "寄回快递单号",
                    "寄回快递公司",
                    "原价",
                    "实际价格",
                    "使用时长",
                    "备注",
                    "创建时间"};
            Object[][] value = new Object[list.size() + 1][n.length];
            for (int m = 0; m < n.length; m++) {
                value[0][m] = n[m];
            }
            for (int i = 0; i < list.size(); i++) {
                value[i + 1][0] = list.get(i).getOrderId();
                value[i + 1][1] = list.get(i).getActivityName();
                value[i + 1][2] = list.get(i).getEquipName();
                value[i + 1][3] = list.get(i).getConsumerId();
                value[i + 1][4] = list.get(i).getConsignee();
                value[i + 1][5] = list.get(i).getPhone();
                value[i + 1][6] = list.get(i).getExpressAddress();
                value[i + 1][7] = list.get(i).getQuantity();
                if(org.springframework.util.StringUtils.isEmpty(list.get(i).getItemPrice())){
                    value[i + 1][8] = "无";
                }else {
                    value[i + 1][8] = list.get(i).getItemPrice();
                }
                if(org.springframework.util.StringUtils.isEmpty(list.get(i).getItemTotalPrice())){
                    value[i + 1][9] = "无";
                }else {
                    value[i + 1][9] = list.get(i).getItemTotalPrice();
                }
                if(org.springframework.util.StringUtils.isEmpty(list.get(i).getItemRealPrice())){
                    value[i + 1][10] = "无";
                }else {
                    value[i + 1][10] = list.get(i).getItemRealPrice();
                }
                value[i + 1][11] = new SimpleDateFormat("yyyy-MM-dd").format(list.get(i).getExpectedDate());
                if(org.springframework.util.StringUtils.isEmpty(list.get(i).getExcode())){
                    value[i + 1][12] = "无";
                }else {
                    value[i + 1][12] = list.get(i).getExcode();
                }
                if (!org.springframework.util.StringUtils.isEmpty(list.get(i).getStatus())) {

                    switch (list.get(i).getStatus().toString()) {
                        case "APPLIED":
                            value[i + 1][13] = "已申请";
                            break;
                        case "PAYED":
                            value[i + 1][13] = "已支付";
                            break;
                        case "CONFIRMED":
                            value[i + 1][13] = "已确认";
                            break;
                        case "DELIVERED":
                            value[i + 1][13] = "已发货";
                            break;
                        case "BACK":
                            value[i + 1][13] = "已寄回";
                            break;
                        case "FINISHED":
                            value[i + 1][13] = "已完成";
                            break;
                        case "CLOSED":
                            value[i + 1][13] = "已关闭";
                            break;
                        case "CANCELED":
                            value[i + 1][13] = "已取消";
                            break;
                    }
                }
                if(org.springframework.util.StringUtils.isEmpty(list.get(i).getMachineOrderNo())){
                    value[i + 1][14] = "无";
                }else {
                    value[i + 1][14] = list.get(i).getMachineOrderNo();
                }
                if(org.springframework.util.StringUtils.isEmpty(list.get(i).getExpressOutNum())){
                    value[i + 1][15] = "无";
                }else {
                    value[i + 1][15] = list.get(i).getExpressOutNum();
                }
                if(org.springframework.util.StringUtils.isEmpty(list.get(i).getExpressOutCompany())){
                    value[i + 1][16] = "无";
                }else {
                    value[i + 1][16] = list.get(i).getExpressOutCompany();
                }
                if(org.springframework.util.StringUtils.isEmpty(list.get(i).getExpressBackNum())){
                    value[i + 1][17] = "无";
                }else {
                    value[i + 1][17] = list.get(i).getExpressBackNum();
                }
                if(org.springframework.util.StringUtils.isEmpty(list.get(i).getExpressBackCompany())){
                    value[i + 1][18] = "无";
                }else {
                    value[i + 1][18] = list.get(i).getExpressBackCompany();
                }
                value[i + 1][19] = list.get(i).getTotalPrice();
                value[i + 1][20] = list.get(i).getRealPay();
                value[i + 1][21] = list.get(i).getIntervalDate();
                if(org.springframework.util.StringUtils.isEmpty(list.get(i).getDescription())){
                    value[i + 1][22] = "无";
                }else {
                    value[i + 1][22] = list.get(i).getDescription();
                }
                value[i + 1][23] = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(list.get(i).getCreateTime());
            }
            HSSFRow row[]=new HSSFRow[list.size()+1];
            HSSFCell cell[]=new HSSFCell[n.length];
            for(int i=0;i<row.length;i++){
                row[i]=sheet.createRow(i);
                for(int j=0;j<cell.length;j++){
                    cell[j]=row[i].createCell(j);
                    cell[j].setCellValue(value[i][j].toString());
                }
            }
            OutputStream os = response.getOutputStream();
            response.setContentType("application/vnd.ms-excel;charset=utf-8");
            response.addHeader("Content-Disposition", "attachment;filename="+fileName);
            wb.write(os);
            os.close();
        }catch (IOException e) {
            e.printStackTrace();
        }
        File file = new File(baseDir);
        if (!file.exists()) {
            logger.error("未能找到文件: " + baseDir);
        }
        try {
            Workbook book = WorkbookFactory.create(file);
            ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
            book.write(byteArrayOutputStream);
            response.setContentType("application/vnd.ms-excel;charset=utf-8");
            response.addHeader("Content-Disposition", "attachment;filename="+fileName);
            response.setContentLength(byteArrayOutputStream.size());
            ServletOutputStream outputstream = response.getOutputStream();
            byteArrayOutputStream.writeTo(outputstream);
            byteArrayOutputStream.close();
            outputstream.flush();
            outputstream.close();
        } catch (Exception e) {
            logger.error(e.getMessage());
        }
        return "";
    }

    /**
     * 上传文件进行存储和解析
     * @param request
     * @return
     */
    @RequestMapping(value = "/order/upload", method = RequestMethod.POST, consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResultData upload(MultipartHttpServletRequest request) {
        ResultData result = new ResultData();
        //存储文件
        MultipartFile file = request.getFile("file");
        String name = file.getOriginalFilename();
        File base = null;
        try {
            base = new File(baseDir);
            if (!base.exists()) base.mkdirs();
        } catch (Exception e) {
            if (base != null) base.mkdirs();
        }
        File target = new File( baseDir + File.separator + name);
        try {
            file.transferTo(target);
        } catch (Exception e) {
            logger.error("文件: " + name + "处理失败, " + e.getMessage());
            result.setResponseCode(ResponseCode.RESPONSE_ERROR);
            result.setDescription("文件上传失败");
            return result;
        }
        //解析文件
        try {
            Workbook book = WorkbookFactory.create(target);
            int nums = book.getNumberOfSheets();
            if (nums <= 0) {
                result.setResponseCode(ResponseCode.RESPONSE_ERROR);
                result.setDescription("请确保上传的文件中有列表数据");
                return result;
            }
            //获取第一页的数据
            Sheet sheet = book.getSheetAt(0);
            JSONArray data = ExcelUtil.decodeDriftOrder(sheet);
            result.setData(data);
        } catch (Exception e) {
            result.setResponseCode(ResponseCode.RESPONSE_ERROR);
            result.setDescription("文件未能成功解析");
        }
        return result;
    }

    /**
     * 根据上传excel表格更新数据库drift_order和order_express表
     * @param form
     * @return
     */
    @PostMapping("/order/changeStatus")
    public ResultData changeStatus(ChangeOrderStatusForm form) {
        ResultData result = new ResultData();
        if (org.springframework.util.StringUtils.isEmpty(form.getExpressNum()) || org.springframework.util.StringUtils.isEmpty(form.getMachineOrderNo())
                || org.springframework.util.StringUtils.isEmpty(form.getOrderId()) || org.springframework.util.StringUtils.isEmpty(form.getCompany()) ) {
            result.setResponseCode(ResponseCode.RESPONSE_ERROR);
            result.setDescription("请输入所有的安装任务用户相关的信息，如果为空填无");
            return result;
        }
        String expressNum = form.getExpressNum().trim();
        String machineOrderNo = form.getMachineOrderNo().trim();
        String orderId = form.getOrderId().trim();
        String company = form.getCompany().trim();
        String description = form.getDescription().trim();
        if(expressNum.equals("无")){//将“无”转换为空值
            expressNum = null;
        }
        if(machineOrderNo.equals("无")){
            machineOrderNo = "";
        }
        if(company.equals("无")){
            company = null;
        }
        if(description.equals("无")){
            description = "";
        }
        String account = (String) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        result = driftService.changeStatus(orderId,machineOrderNo,expressNum,company,description,account);
//        String message = "管理员通过上传excel更新了订单,快递单号："+expressNum+"、快递公司："+company+"、机器码："+machineOrderNo;
//        driftService.createAction(orderId,message,"admin");
        return result;
    }

    /**
     * 根据orderId查询订单操作日志
     * @param orderId
     * @return
     */
    @GetMapping("/action/selectByOrderId")
    ResultData actionSelect(String orderId){
        ResultData result = new ResultData();
        if(StringUtils.isEmpty(orderId)){
            result.setResponseCode(ResponseCode.RESPONSE_ERROR);
            result.setDescription("请提供orderId");
            return result;
        }
        result = driftService.selectAction(orderId);
        return result;
    }

    /**
     * 获取订单取消记录用于退款
     * @param status
     * @return
     */
    @GetMapping("/cancel/record/select")
    ResultData selectCancelRecord(String status){
        ResultData result = new ResultData();
        if(org.springframework.util.StringUtils.isEmpty(status)){
            result.setResponseCode(ResponseCode.RESPONSE_ERROR);
            result.setDescription("请提供查询条件");
            return result;
        }
        result = driftService.selectCancelRecord(status);
        return result;
    }

    /**
     * 根据orderId更新取消的订单为已完成
     * @param orderId
     * @return
     */
    @PostMapping("/cancel/record/update")
    ResultData updateCancelRecord(String orderId){
        ResultData result = new ResultData();
        if(org.springframework.util.StringUtils.isEmpty(orderId)){
            result.setResponseCode(ResponseCode.RESPONSE_ERROR);
            result.setDescription("请提供orderId");
            return result;
        }
        result = driftService.updateCancelRecord(orderId);
        if(result.getResponseCode()==ResponseCode.RESPONSE_OK){
            String account = (String) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
//            Admin admin = JSONObject.parseObject(JSONObject.toJSONString(authService.getAdmin(account).getData()),Admin.class);
            Admin admin = JSONArray.parseArray(JSONObject.toJSONString(authService.getAdmin(account).getData()),Admin.class).get(0);
            String message = admin.getUsername()+"将退款订单标记为退款成功";
            driftService.createAction(orderId,message,admin.getAdminId());
        }
        return result;
    }
}
