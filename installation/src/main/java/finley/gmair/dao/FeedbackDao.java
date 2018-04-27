package finley.gmair.dao;

import finley.gmair.model.installation.Feedback;
import finley.gmair.util.ResultData;

import java.util.Map;

public interface FeedbackDao {
    ResultData insertFeedback(Feedback feedback);

    ResultData queryFeedback(Map<String, Object> condition);

}
