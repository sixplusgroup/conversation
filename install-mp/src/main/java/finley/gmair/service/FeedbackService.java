package finley.gmair.service;

import finley.gmair.model.installation.Feedback;
import finley.gmair.util.ResultData;

import java.util.Map;

public interface FeedbackService {

    ResultData createFeedback(Feedback feedback);

    ResultData fetchFeedback(Map<String, Object> condition);

    //ResultData updateFeedback(Feedback feedback);
}
