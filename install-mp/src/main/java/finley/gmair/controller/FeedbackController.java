package finley.gmair.controller;

import finley.gmair.form.installation.FeedbackForm;
import finley.gmair.model.installation.Feedback;
import finley.gmair.service.FeedbackService;
import finley.gmair.util.ResponseCode;
import finley.gmair.util.ResultData;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;


@RestController
@RequestMapping("/install-mp/feedback")
public class FeedbackController {

    @Autowired
    private FeedbackService feedbackService;

    //工人提交反馈表时触发,创建反馈表单
    @RequestMapping(method = RequestMethod.POST, value="/create")
    public ResultData create(FeedbackForm form)
    {
        ResultData result = new ResultData();

        String assignId = form.getAssignId().trim();
        String memberPhone = form.getMemberPhone().trim();
        String feedbackContent = form.getFeedbackContent().trim();
        String status = form.getStatus();

        //check whether input is empty
        if(StringUtils.isEmpty(assignId) || StringUtils.isEmpty(memberPhone) || StringUtils.isEmpty(feedbackContent)){
            result.setResponseCode(ResponseCode.RESPONSE_ERROR);
            result.setDescription("Please provide all information");
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
}
