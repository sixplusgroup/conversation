package finley.gmair.controller;

import finley.gmair.form.drift.ActivityForm;
import finley.gmair.model.drift.Activity;
import finley.gmair.service.ActivityService;
import finley.gmair.util.ResponseCode;
import finley.gmair.util.ResultData;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping("/assemble/activity")
public class ActivityController {

    @Autowired
    private ActivityService activityService;

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
