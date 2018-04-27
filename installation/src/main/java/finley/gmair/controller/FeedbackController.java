package finley.gmair.controller;

import finley.gmair.form.installation.FeedbackForm;
import finley.gmair.model.installation.Feedback;
import finley.gmair.service.FeedbackService;
import finley.gmair.util.ResponseCode;
import finley.gmair.util.ResultData;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping("/installation/feedback")
public class FeedbackController {
    @Autowired
    private FeedbackService feedbackService;

    @PostMapping("/create")
    public ResultData create(FeedbackForm form)
    {
        ResultData result = new ResultData();

        String assignId = form.getAssignId().trim();
        String memberPhone = form.getMemberPhone().trim();
        String feedbackContent = form.getFeedbackContent().trim();
        String status = form.getStatus();

        //check whether input is empty
        if(StringUtils.isEmpty(assignId)){
            result.setResponseCode(ResponseCode.RESPONSE_ERROR);
            result.setDescription("Please provide the qrcode");
            return result;
        }
        if(StringUtils.isEmpty(memberPhone)){
            result.setResponseCode(ResponseCode.RESPONSE_ERROR);
            result.setDescription("Please provide the phone");
            return result;
        }
        if(StringUtils.isEmpty(feedbackContent)){
            result.setResponseCode(ResponseCode.RESPONSE_ERROR);
            result.setDescription("Please provide the feedback content");
            return result;
        }

        //create the the feedback
        Feedback feedback = new Feedback(assignId,memberPhone,feedbackContent,status);
        ResultData response = feedbackService.createFeedback(feedback);
        if (response.getResponseCode() != ResponseCode.RESPONSE_OK) {
            result.setResponseCode(ResponseCode.RESPONSE_ERROR);
            result.setDescription("Fail to create the feedback");
            return result;
        }
        result.setResponseCode(ResponseCode.RESPONSE_OK);
        result.setData(response.getData());
        result.setDescription("Success to create the feedback");
        return result;
    }

    @GetMapping("/list")
    public ResultData list()
    {
        ResultData result = new ResultData();
        
        Map<String, Object> condition = new HashMap<>();
        condition.put("blockFlag",false);
        ResultData response = feedbackService.fetchFeedback(condition);
        if(response.getResponseCode()==ResponseCode.RESPONSE_ERROR)
        {
            result.setResponseCode(ResponseCode.RESPONSE_ERROR);
            result.setDescription("Fail to feedback info");
        }
        else if(response.getResponseCode()==ResponseCode.RESPONSE_NULL)
        {
            result.setResponseCode(ResponseCode.RESPONSE_NULL);
            result.setDescription("No feedback info at the moment");
        }
        else
        {
            result.setResponseCode(ResponseCode.RESPONSE_OK);
            result.setData(response.getData());
            result.setDescription("Success to feedback info");
        }
        return result;
    }

}
