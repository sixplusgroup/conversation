package finley.gmair.service;

import finley.gmair.util.ResultData;
import org.springframework.cloud.netflix.feign.FeignClient;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

@FeignClient("install-agent")
public interface FeedbackService {

    @PostMapping("/installation/feedback/create")
    ResultData createFeedback(@RequestParam("assignId") String assignId, @RequestParam("feedbackContent") String feedbackContent);
}
