package finley.gmair.controller;

import finley.gmair.form.drift.ActivityForm;
import finley.gmair.service.ActivityService;
import finley.gmair.util.ResultData;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/assemble/activity")
public class ActivityController {

    @Autowired
    private ActivityService activityService;

    @PostMapping(value = "/create")
    public ResultData createDriftActivity(ActivityForm form) {
        ResultData result = new ResultData();
        return result;
    }
}
