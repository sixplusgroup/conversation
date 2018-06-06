package finley.gmair.model.installation;

import finley.gmair.model.Entity;

public class Feedback extends Entity {
    private String feedbackId;
    private String assignId;
    private String feedbackContent;

    public Feedback() {
        super();
    }

    public Feedback(String assignId, String feedbackContent) {
        this();
        this.assignId = assignId;
        this.feedbackContent = feedbackContent;
    }

    public String getFeedbackId() {
        return feedbackId;
    }

    public void setFeedbackId(String feedbackId) {
        this.feedbackId = feedbackId;
    }

    public String getAssignId() {
        return assignId;
    }

    public void setAssignId(String assignId) {
        this.assignId = assignId;
    }

    public String getFeedbackContent() {
        return feedbackContent;
    }

    public void setFeedbackContent(String feedbackContent) {
        this.feedbackContent = feedbackContent;
    }
}
