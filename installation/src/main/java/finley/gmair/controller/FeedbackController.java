package finley.gmair.controller;

import finley.gmair.form.installation.FeedbackForm;
import finley.gmair.model.installation.Feedback;
import finley.gmair.service.FeedbackService;
import finley.gmair.util.ResponseCode;
import finley.gmair.util.ResultData;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping("/installation/feedback")
public class FeedbackController {
    @Autowired
    private FeedbackService feedbackService;

    @PostMapping("/create")
    public ResultData create(FeedbackForm form) {
        ResultData result = new ResultData();
        //check input
        if (StringUtils.isEmpty(form.getAssignId()) || StringUtils.isEmpty(form.getFeedbackContent())) {
            result.setResponseCode(ResponseCode.RESPONSE_ERROR);
            result.setDescription("Please provide all required information");
            return result;
        }
        Feedback feedback = new Feedback(form.getAssignId().trim(), form.getFeedbackContent().trim());
        ResultData response = feedbackService.createFeedback(feedback);
        if (response.getResponseCode() == ResponseCode.RESPONSE_ERROR) {
            result.setResponseCode(ResponseCode.RESPONSE_ERROR);
            result.setDescription("Fail to create feedback");
            return result;
        }
        result.setData(response.getData());
        return result;
    }

    //管理员查看反馈时触发,拉取工人反馈信息列表
    @RequestMapping(method = RequestMethod.GET, value = "/list")
    public ResultData list() {
        ResultData result = new ResultData();

        Map<String, Object> condition = new HashMap<>();
        condition.put("blockFlag", false);
        ResultData response = feedbackService.fetchFeedback(condition);
        if (response.getResponseCode() == ResponseCode.RESPONSE_ERROR) {
            result.setResponseCode(ResponseCode.RESPONSE_ERROR);
            result.setDescription("Fail to feedback info");
        } else if (response.getResponseCode() == ResponseCode.RESPONSE_NULL) {
            result.setResponseCode(ResponseCode.RESPONSE_NULL);
            result.setDescription("No feedback info at the moment.");
        } else {
            result.setResponseCode(ResponseCode.RESPONSE_OK);
            result.setData(response.getData());
            result.setDescription("Success to feedback info");
        }
        return result;
    }
}
