package finley.gmair.controller;

import finley.gmair.form.drift.ActivityForm;
import finley.gmair.form.drift.EXCodeCreateForm;
import finley.gmair.model.drift.Activity;
import finley.gmair.model.drift.EXCode;
import finley.gmair.service.ActivityService;
import finley.gmair.service.EXCodeService;
import finley.gmair.util.ResponseCode;
import finley.gmair.util.ResultData;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/assemble/activity")
public class ActivityController {

    @Autowired
    private ActivityService activityService;

    @Autowired
    private EXCodeService exCodeService;

    /**
     * the method is used to create activity
     *
     * @return */
    @PostMapping(value = "/create")
    public ResultData createDriftActivity(ActivityForm form) {
        ResultData result = new ResultData();

        //judge the parameter complete or not
        if (StringUtils.isEmpty(form.getGoodsId()) || StringUtils.isEmpty(form.getActivityName()) || StringUtils.isEmpty(form.getRepositorySize())
            || StringUtils.isEmpty(form.getThreshold()) || StringUtils.isEmpty(form.getStartTime()) || StringUtils.isEmpty(form.getEndTime())) {
            result.setResponseCode(ResponseCode.RESPONSE_ERROR);
            result.setDescription("please make sure you fill all the required fields");
            return result;
        }

        //build activity entity
        String goodsId = form.getGoodsId().trim();
        String activityName = form.getActivityName().trim();
        int repositorySize = form.getRepositorySize();
        double threshold = form.getThreshold();
        Date startTime = form.getStartTime();
        Date endTime = form.getEndTime();
        Activity activity = new Activity(goodsId, activityName, repositorySize, threshold, startTime, endTime);
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
     * the method is called to create excode if activity needs
     *
     * @return */
    @PostMapping(value = "/excode/create")
    public ResultData createEXCode(EXCodeCreateForm form) {
        ResultData result = new ResultData();
        if (StringUtils.isEmpty(form.getActivityId()) || StringUtils.isEmpty(form.getNum())) {
            result.setResponseCode(ResponseCode.RESPONSE_ERROR);
            result.setDescription("please make sure you fill all the required fields");
            return result;
        }
        //verify activity whether exists or not by activityId
        String activityId = form.getActivityId();
        Map<String, Object> condition = new HashMap<>();
        condition.put("activityId", activityId);
        ResultData response = activityService.fetchActivity(condition);
        if (response.getResponseCode() != ResponseCode.RESPONSE_OK) {
            result.setResponseCode(ResponseCode.RESPONSE_ERROR);
            result.setDescription("兑换码一致性错误，服务器不予处理");
            return result;
        }

        int num = form.getNum();
        response = exCodeService.createEXCode(activityId, num);
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

    private String generateXls(String activityId) {
        Workbook workbook = new XSSFWorkbook();
        Sheet sheet = workbook.createSheet("EXCode");
        Map<String, Object> condition = new HashMap<>();
        condition.put("activityId", activityId);
        ResultData response = activityService.fetchActivity(condition);
        if (response.getResponseCode() != ResponseCode.RESPONSE_OK) {

        }
        response = exCodeService.fetchEXCode(condition);
        if (response.getResponseCode() != ResponseCode.RESPONSE_OK) {
            return new StringBuffer("The request with activityId: ").append(activityId).append(" can not be executed").toString();
        }

        //set table header
        Row row0 = sheet.createRow(0);
        Cell headCell0 = row0.createCell(0);
        headCell0.setCellValue("序号");
        Cell headCell1 = row0.createCell(1);
        headCell1.setCellValue("活动名");
        Cell headCell2 = row0.createCell(2);
        headCell2.setCellValue("兑换码");

        //insert real data to table
        List<EXCode> exCodes = (List<EXCode>) response.getData();
        for (int row =1, i = 0; i < exCodes.size(); i++, row++) {
            EXCode code = exCodes.get(i);
        }
        return "";
    }

    /**
     * the method is used to select the activity list
     *
     * @return*/
    @GetMapping(value = "list")
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
}
