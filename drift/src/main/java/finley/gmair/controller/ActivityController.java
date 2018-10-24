package finley.gmair.controller;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import finley.gmair.form.drift.ActivityForm;
import finley.gmair.form.drift.EXCodeCreateForm;
import finley.gmair.model.drift.*;
import finley.gmair.service.*;
import finley.gmair.util.DriftProperties;
import finley.gmair.util.IDGenerator;
import finley.gmair.util.ResponseCode;
import finley.gmair.util.ResultData;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletResponse;
import java.io.*;
import java.text.SimpleDateFormat;
import java.util.*;

@RestController
@RequestMapping("/drift/activity")
public class ActivityController {

    @Autowired
    private ActivityService activityService;

    @Autowired
    private EXCodeService exCodeService;

    @Autowired
    private EquipmentService equipmentService;

    @Autowired
    private ChannelService channelService;

    @Autowired
    private MachineService machineService;

    @Autowired
    private QrExCodeService qrExCodeService;

    /**
     * the method is used to create activity
     *
     * @return
     * */
    @PostMapping(value = "/create")
    public ResultData createDriftActivity(ActivityForm form) throws Exception {
        ResultData result = new ResultData();

        //judge the parameter complete or not
        if (StringUtils.isEmpty(form.getGoodsId()) || StringUtils.isEmpty(form.getActivityName()) || StringUtils.isEmpty(form.getRepositorySize())
                || StringUtils.isEmpty(form.getThreshold()) || StringUtils.isEmpty(form.getReservableDays()) || StringUtils.isEmpty(form.getStartTime())
                || StringUtils.isEmpty(form.getEndTime())) {
            result.setResponseCode(ResponseCode.RESPONSE_ERROR);
            result.setDescription("please make sure you fill all the required fields");
            return result;
        }

        //build activity entity
        String goodsId = form.getGoodsId().trim();
        String activityName = form.getActivityName().trim();
        int repositorySize = form.getRepositorySize();
        double threshold = form.getThreshold();
        int reservableDays = form.getReservableDays();
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
        Date start = sdf.parse(form.getStartTime());
        Date end = sdf.parse(form.getEndTime());

        Activity activity = new Activity(goodsId, activityName, repositorySize, threshold, reservableDays, start, end);
        ResultData response = activityService.createActivity(activity);
        if (response.getResponseCode() == ResponseCode.RESPONSE_ERROR) {
            result.setResponseCode(ResponseCode.RESPONSE_ERROR);
            result.setDescription("Fail to store activity to database");
        }
        if (response.getResponseCode() == ResponseCode.RESPONSE_OK) {
            result.setResponseCode(ResponseCode.RESPONSE_OK);
            result.setData(response.getData());
        }
        return result;
    }

    /**
     * the method is called to create equipment
     *
     * @return
     * */
    @PostMapping(value = "/equip/create")
    public ResultData createEquipment(String equipmentName) {
        ResultData result = new ResultData();
        if (StringUtils.isEmpty(equipmentName)) {
            result.setResponseCode(ResponseCode.RESPONSE_ERROR);
            result.setDescription("Please make sure you fill all the required fields");
            return result;
        }

        Equipment equipment = new Equipment(equipmentName);
        ResultData response = equipmentService.createEquipment(equipment);
        if (response.getResponseCode() != ResponseCode.RESPONSE_OK) {
            result.setResponseCode(ResponseCode.RESPONSE_ERROR);
            result.setDescription("Fail to store equipment to database");
            return result;
        }
        result.setResponseCode(ResponseCode.RESPONSE_OK);
        result.setData(response.getData());
        return result;
    }

    /**
     * the method is called to build equipment_activity relationship
     * parameters: 1.activityId, 2.equipmentId
     *
     * @return
     * */
    @PostMapping(value = "/equipactivity/bind/create")
    public ResultData bind(String activityId, String equipmentId) {
        ResultData result = new ResultData();
        if (StringUtils.isEmpty(activityId) || StringUtils.isEmpty(equipmentId)) {
            result.setResponseCode(ResponseCode.RESPONSE_ERROR);
            result.setDescription("Please make sure you fill all the required fields");
            return result;
        }

        //check the activity and equipment all exist or not
        Map<String, Object> condition = new HashMap<>();
        condition.put("activityId", activityId);
        condition.put("blockFlag", false);
        ResultData response = activityService.fetchActivity(condition);
        if (response.getResponseCode() != ResponseCode.RESPONSE_OK) {
            result.setResponseCode(ResponseCode.RESPONSE_ERROR);
            result.setDescription("Fail to query activity with activityId");
            return result;
        }
        condition.remove("activityId");
        condition.put("equipId", equipmentId);
        response = equipmentService.fetchEquipment(condition);
        if (response.getResponseCode() != ResponseCode.RESPONSE_OK) {
            result.setResponseCode(ResponseCode.RESPONSE_ERROR);
            result.setDescription("Fail to query equipment with equipmentId");
            return result;
        }

        //after checking, build entity and insert
        EquipActivity equipActivity = new EquipActivity(equipmentId, activityId);
        response = equipmentService.createRelationship(equipActivity);
        if (response.getResponseCode() != ResponseCode.RESPONSE_OK) {
            result.setResponseCode(ResponseCode.RESPONSE_ERROR);
            result.setDescription("Fail to store to database");
            return result;
        }
        result.setResponseCode(ResponseCode.RESPONSE_OK);
        result.setData(response.getData());
        return result;
    }

    /**
     * the method is used to select actual activity by activityId
     *
     * @return
     * */
    @PostMapping(value = "/getActivity/byId")
    public ResultData getActivityById(String activityId) {
        ResultData result = new ResultData();
        Map<String, Object> condition = new HashMap<>();
        if (!StringUtils.isEmpty(activityId)) {
            condition.put("activityId", activityId);
        }
        condition.put("blockFlag", false);
        ResultData response = activityService.fetchActivity(condition);
        if (response.getResponseCode() == ResponseCode.RESPONSE_NULL) {
            result.setResponseCode(ResponseCode.RESPONSE_NULL);
            result.setDescription("No activity found from database by activityId");
        }
        if (response.getResponseCode() == ResponseCode.RESPONSE_ERROR) {
            result.setResponseCode(ResponseCode.RESPONSE_ERROR);
            result.setDescription("Query activity error, please try again later");
        }
        if (response.getResponseCode() == ResponseCode.RESPONSE_OK) {
            result.setResponseCode(ResponseCode.RESPONSE_OK);
            Activity activity = ((List<Activity>) response.getData()).get(0);
            result.setData(activity);
        }
        return result;
    }

    /**
     * the method is called to create excode if activity needs
     *
     * @return
     * */
    @PostMapping(value = "/excode/create")
    public ResultData createEXCode(EXCodeCreateForm form) {
        ResultData result = new ResultData();
        if (StringUtils.isEmpty(form.getActivityId()) || StringUtils.isEmpty(form.getNum())
                || StringUtils.isEmpty(form.getPrice()) || StringUtils.isEmpty(form.getChannelId())) {
            result.setResponseCode(ResponseCode.RESPONSE_ERROR);
            result.setDescription("please make sure you fill all the required fields");
            return result;
        }
        //verify activity whether exists or not by activityId
        String activityId = form.getActivityId();
        Map<String, Object> condition = new HashMap<>();
        condition.put("activityId", activityId);
        condition.put("blockFlag", false);
        ResultData response = activityService.fetchActivity(condition);
        if (response.getResponseCode() != ResponseCode.RESPONSE_OK) {
            result.setResponseCode(ResponseCode.RESPONSE_ERROR);
            result.setDescription("活动一致性错误，服务器不予处理");
            return result;
        }

        //verify channel correct or not
        String channelId = form.getChannelId();
        condition.clear();
        condition.put("channelId", channelId);
        condition.put("blockFlag", false);
        response = channelService.fetchChannel(condition);
        if (response.getResponseCode() != ResponseCode.RESPONSE_OK) {
            result.setResponseCode(ResponseCode.RESPONSE_ERROR);
            result.setDescription("引流一致性错误，服务器不予处理");
            return result;
        }

        DriftChannel channel = ((List<DriftChannel>) response.getData()).get(0);

        String channelName = channel.getChannelName();
        int num = form.getNum();
        double price = form.getPrice();
        response = exCodeService.createEXCode(activityId, channelName, num, price);
        if (response.getResponseCode() == ResponseCode.RESPONSE_OK) {
            result.setResponseCode(ResponseCode.RESPONSE_OK);
            new Thread(() -> {
                String filename = generateXls(activityId);
            }).start();
            result.setDescription("Succeed to generate EXCode");
            return result;
        }
        result.setResponseCode(ResponseCode.RESPONSE_ERROR);
        result.setDescription("Sorry, the EXCodes are not generated as expected, please try again");
        return result;
    }

    /**
     * This method is used to generate the excode xls when create batch excode
     * method is private, just called in create batch
     *
     * @return
     */
    private String generateXls(String activityId) {
        Workbook workbook = new XSSFWorkbook();
        Sheet sheet = workbook.createSheet("EXCode");
        Map<String, Object> condition = new HashMap<>();
        condition.put("activityId", activityId);
        ResultData response = activityService.fetchActivity(condition);
        if (response.getResponseCode() != ResponseCode.RESPONSE_OK) {
            return new StringBuffer("The request with activityId: ").append(activityId).append(" can not be executed").toString();
        }
        Activity activity = ((List<Activity>) response.getData()).get(0);
        response = exCodeService.fetchEXCode(condition);
        if (response.getResponseCode() != ResponseCode.RESPONSE_OK) {
            return new StringBuffer("The request with activityId: ").append(activityId).append(" is error").toString();
        }

        //set table header
        Row row0 = sheet.createRow(0);
        Cell headCell0 = row0.createCell(0);
        headCell0.setCellValue("序号");
        Cell headCell1 = row0.createCell(1);
        headCell1.setCellValue("活动名");
        Cell headCell2 = row0.createCell(2);
        headCell2.setCellValue("兑换码");
        Cell headCell3 = row0.createCell(3);
        headCell3.setCellValue("价格");

        //insert real data to table
        List<EXCode> exCodes = (List<EXCode>) response.getData();
        for (int row =1, i = 0; i < exCodes.size(); i++, row++) {
            EXCode code = exCodes.get(i);
            Row current = sheet.createRow(row);
            Cell serial = current.createCell(0);
            serial.setCellValue(i + 1);
            Cell activityName = current.createCell(1);
            activityName.setCellValue(activity.getActivityName().trim());
            Cell codeValue = current.createCell(2);
            codeValue.setCellValue(code.getCodeValue().trim());
            Cell price = current.createCell(3);
            price.setCellValue(code.getPrice());
        }

        //create file
        String base = DriftProperties.getValue("excode_storage_path");
        File directory = new File(new StringBuffer(base).append(DriftProperties.getValue("excode_batch")).toString());
        if (!directory.exists()) {
            directory.mkdir();
        }
        String tempSerial = IDGenerator.generate("EXC");
        File file = new File(
                new StringBuffer(base).append(DriftProperties.getValue("excode_batch")).append(tempSerial).append(".xlsx").toString());
        if (!file.exists()) {
            try {
                file.createNewFile();
            } catch (IOException e) {
                e.printStackTrace();
                return e.getMessage();
            }
        }

        //store data to file
        try {
            OutputStream outputStream = new FileOutputStream(file);
            workbook.write(outputStream);
            outputStream.flush();
            outputStream.close();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
            return e.getMessage();
        } catch (IOException e) {
            e.printStackTrace();
            return e.getMessage();
        }
        return tempSerial;
    }

    /**
     * This method is used to download a batch of excode
     *
     * @return
     */
    @ResponseBody
    @GetMapping(value = "/download/{filename}")
    public void download(@PathVariable("filename") String filename, HttpServletResponse response) {
        File file = null;
        if (filename.startsWith("EXC")) {
            filename = filename + ".xlsx";
            file = new File(DriftProperties.getValue("excode_storage_path") + DriftProperties.getValue("excode_batch") + filename);
        }
        BufferedInputStream inputStream = null;
        BufferedOutputStream outputStream = null;
        try {
            inputStream = new BufferedInputStream(new FileInputStream(file));
            response.setCharacterEncoding("UTF-8");
            response.setContentType("application/octet-stream");
            response.setHeader("Content-disposition", "attachment; filename=" + filename);
            outputStream = new BufferedOutputStream(response.getOutputStream());
            byte[] buffer = new byte[2048];
            int bytesRead;
            while (-1 != (bytesRead = inputStream.read(buffer, 0, buffer.length))) {
                outputStream.write(buffer, 0, bytesRead);
            }
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            try {
                if (inputStream != null) {
                    inputStream.close();
                }
                if (outputStream != null) {
                    outputStream.close();
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    /**
     * The method is called to exchange excoede
     * 1. check channel correct or not
     * 2. if buy machine, provide qrcode, store to database
     *    if activityVIP, only provide channelId
     * @return
     * */
    @PostMapping(value = "/excode/exchange")
    public ResultData excodeExchange(String channelId, String qrcode) {
        ResultData result = new ResultData();
        if (StringUtils.isEmpty(channelId)) {
            result.setResponseCode(ResponseCode.RESPONSE_ERROR);
            result.setDescription("Please make sure you fill all the required fields");
            return result;
        }
        //channelId不为空，判断channel
        Map<String, Object> condition = new HashMap<>();
        condition.put("channelId", channelId);
        condition.put("blockFlag", false);
        ResultData response = channelService.fetchChannel(condition);
        if (response.getResponseCode() != ResponseCode.RESPONSE_OK) {
            result.setResponseCode(ResponseCode.RESPONSE_ERROR);
            result.setDescription("Channel match errors");
            return result;
        }

        DriftChannel channel = ((List<DriftChannel>) response.getData()).get(0);
        String channelName = channel.getChannelName();

        condition.clear();
        condition.put("channel", channelName);
        condition.put("status", 0);
        condition.put("blockFlag", false);
        response = exCodeService.fetchEXCode(condition);
        if (response.getResponseCode() != ResponseCode.RESPONSE_OK) {
            result.setResponseCode(ResponseCode.RESPONSE_ERROR);
            result.setDescription("System errors, please try again");
            return result;
        }
        EXCode code = ((List<EXCode>) response.getData()).get(0);
        //根据channelName判断是哪类兑换
        if (channelName.contains("果麦")) {
            if (StringUtils.isEmpty(qrcode)) {
                result.setResponseCode(ResponseCode.RESPONSE_ERROR);
                result.setDescription("No qrcode");
                return result;
            }
            response = machineService.checkQrcode(qrcode);
            if (response.getResponseCode() != ResponseCode.RESPONSE_OK) {
                result.setResponseCode(ResponseCode.RESPONSE_ERROR);
                result.setDescription("Qrcode errors");
                return result;
            }
            condition.clear();
            condition.put("qrcode", qrcode);
            response = qrExCodeService.fetchQrExCode(condition);
            if (response.getResponseCode() != ResponseCode.RESPONSE_NULL) {
                result.setResponseCode(ResponseCode.RESPONSE_ERROR);
                result.setDescription("The qrcode is already exchanged");
                return result;
            }

            String codeValue = code.getCodeValue();
            QR_EXcode qr_eXcode = new QR_EXcode(qrcode, codeValue);
            new Thread(() -> {
                qrExCodeService.createQrExCode(qr_eXcode);
            }).start();
        }
        condition.clear();
        condition.put("codeId", code.getCodeId());
        condition.put("status", 1);
        response = exCodeService.modifyEXCode(condition);
        if (response.getResponseCode() != ResponseCode.RESPONSE_OK) {
            result.setResponseCode(ResponseCode.RESPONSE_ERROR);
            result.setDescription("Fail to exchange");
            return result;
        }
        result.setResponseCode(ResponseCode.RESPONSE_OK);
        result.setData(code);
        result.setDescription("Succeed to exchange");
        return result;
    }

    /**
     * the method is used to select the activity list
     *
     * @return
     * */
    @GetMapping(value = "/list")
    public ResultData getActivity() {
         ResultData result = new ResultData();
         Map<String, Object> condition = new HashMap<>();
         condition.put("blockFlag", false);
         ResultData response = activityService.fetchActivity(condition);
         switch (response.getResponseCode()) {
             case RESPONSE_NULL:
                 result.setResponseCode(ResponseCode.RESPONSE_NULL);
                 result.setDescription("No activity found");
                 break;
             case RESPONSE_ERROR:
                 result.setResponseCode(ResponseCode.RESPONSE_ERROR);
                 result.setDescription("Query error,try again later");
                 break;
             case RESPONSE_OK:
                 result.setResponseCode(ResponseCode.RESPONSE_OK);
                 result.setData(response.getData());
                 break;
         }
         return result;
    }

    /**
     * The method is called to update activity with parameters by activity_id
     * available parameters:
     * 1. size, 2. threshold, 3. reservable_days, 4. end_time
     *
     * @return
     * */
    @PostMapping(value = "/update")
    public ResultData updateActivity(String activityId, Integer repositorySize, Double threshold, Integer reservableDays, String endTime) {
         ResultData result = new ResultData();
         Map<String, Object> condition = new HashMap<>();
         //if no activityId, don't allow to update
         if (StringUtils.isEmpty(activityId)) {
             result.setResponseCode(ResponseCode.RESPONSE_ERROR);
             result.setDescription("Can't update activity with no activityId");
             return result;
         }
         condition.put("activityId", activityId);
         //put exist parameters to map
         if (!StringUtils.isEmpty(repositorySize)) {
             condition.put("repositorySize", repositorySize);
         }
         if (!StringUtils.isEmpty(threshold)) {
             condition.put("threshold", threshold);
         }
         if (!StringUtils.isEmpty(reservableDays)) {
             condition.put("reservableDays", reservableDays);
         }
         if (!StringUtils.isEmpty(endTime)) {
             condition.put("endTime", endTime);
         }

         ResultData response = activityService.modifyActivity(condition);
         if (response.getResponseCode() != ResponseCode.RESPONSE_OK) {
             result.setResponseCode(ResponseCode.RESPONSE_ERROR);
             result.setDescription("Fail to update activity");
             return result;
         }
         result.setResponseCode(ResponseCode.RESPONSE_OK);
         return result;
    }

    @GetMapping(value = "/getEquip/by/{activityId}")
    public ResultData getEquipmentByActivityId(@PathVariable("activityId") String activityId) {
        ResultData result = new ResultData();
        if (StringUtils.isEmpty(activityId)) {
            result.setResponseCode(ResponseCode.RESPONSE_ERROR);
            result.setDescription("Please make sure you fill all the required fields");
            return result;
        }
        Map<String, Object> condition = new HashMap<>();
        condition.put("activityId", activityId);
        condition.put("blockFlag", false);
        ResultData response = activityService.fetchActivityEquipment(condition);
        if (response.getResponseCode() == ResponseCode.RESPONSE_ERROR) {
            result.setResponseCode(ResponseCode.RESPONSE_ERROR);
            result.setDescription("Fail to get equipment by activityId");
        } else if (response.getResponseCode() == ResponseCode.RESPONSE_NULL) {
            result.setResponseCode(ResponseCode.RESPONSE_NULL);
            result.setDescription("No equipment found by activityId");
        } else {
            result.setResponseCode(ResponseCode.RESPONSE_OK);
            result.setData(response.getData());
        }
        return result;
    }
}
