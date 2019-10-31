package finley.gmair.controller;


import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import finley.gmair.form.drift.ChangeOrderStatusForm;
import finley.gmair.form.installation.AssignForm;
import finley.gmair.model.drift.DriftOrderPanel;
import finley.gmair.model.installation.AssignReport;
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

@CrossOrigin
@RestController
@RequestMapping("/management/drift")
public class DriftController {
    private Logger logger = LoggerFactory.getLogger(DriftController.class);

    @Autowired
    private DriftService driftService;

    @Value("${temp_path}")
    private String baseDir;

    @GetMapping("/order/list")
    ResultData driftOrderList(String startTime,String endTime,String status,String search){
        return driftService.driftOrderList(startTime,endTime,status,search);
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
        result = driftService.cancelOrder(orderId);
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
        return result;
    }

    /**
     * 下载当前搜索到的订单
     * @param startTime
     * @param endTime
     * @param status
     * @param search
     * @param response
     * @return
     */
    @GetMapping("/order/download")
    String order_download(String startTime,String endTime,String status,String search,HttpServletResponse response){
        ResultData result=new ResultData();
        result = driftService.driftOrderList(startTime,endTime,status,search);
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
                    "使用日期",
                    "优惠码",
                    "机器码",
                    "快递单号",
                    "快递公司",
                    "订单状态",
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
                value[i + 1][8] = new SimpleDateFormat("yyyy-MM-dd").format(list.get(i).getExpectedDate());
                if(org.springframework.util.StringUtils.isEmpty(list.get(i).getExcode())){
                    value[i + 1][9] = "无";
                }else {
                    value[i + 1][9] = list.get(i).getExcode();
                }
                if(org.springframework.util.StringUtils.isEmpty(list.get(i).getMachineOrderNo())){
                    value[i + 1][10] = "无";
                }else {
                    value[i + 1][10] = list.get(i).getMachineOrderNo();
                }
                if(org.springframework.util.StringUtils.isEmpty(list.get(i).getExpressNum())){
                    value[i + 1][11] = "无";
                }else {
                    value[i + 1][11] = list.get(i).getExpressNum();
                }
                if(org.springframework.util.StringUtils.isEmpty(list.get(i).getCompany())){
                    value[i + 1][12] = "无";
                }else {
                    value[i + 1][12] = list.get(i).getCompany();
                }
                if(org.springframework.util.StringUtils.isEmpty(list.get(i).getStatus())){
                    value[i + 1][13] = "无";
                }else {
                    value[i + 1][13] = DriftUtil.setStatus(list.get(i).getStatus().getValue());
                }
                value[i + 1][14] = list.get(i).getTotalPrice();
                value[i + 1][15] = list.get(i).getRealPay();
                value[i + 1][16] = list.get(i).getIntervalDate();
                if(org.springframework.util.StringUtils.isEmpty(list.get(i).getDescription())){
                    value[i + 1][17] = "无";
                }else {
                    value[i + 1][17] = list.get(i).getDescription();
                }
                value[i + 1][18] = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(list.get(i).getCreateTime());
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
     * 根据上传excel表格更新数据库drift_order和eorder_xpress表
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
            machineOrderNo = null;
        }
        if(company.equals("无")){
            company = null;
        }
        if(description.equals("无")){
            description = null;
        }
        result = driftService.changeStatus(orderId,machineOrderNo,expressNum,company,description);
        return result;
    }
}
