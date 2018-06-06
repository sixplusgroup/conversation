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
    @RequestMapping(method = RequestMethod.POST, value = "/create")
    public ResultData create(FeedbackForm form) {
        return feedbackService.createFeedback(form.getAssignId(), form.getFeedbackContent());
    }
}
