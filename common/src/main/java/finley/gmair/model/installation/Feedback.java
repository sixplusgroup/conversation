package finley.gmair.model.installation;

import finley.gmair.model.Entity;

public class Feedback extends Entity {
    private String feedbackId;
    private String assignId;
    private String memberPhone;
    private String feedbackContent;
    private String status;
    public Feedback()
    {
        super();
    }

    public Feedback(String assignId, String memberPhone, String feedbackContent, String status) {
        this();
        this.assignId = assignId;
        this.memberPhone = memberPhone;
        this.feedbackContent = feedbackContent;
        this.status = status;
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

    public String getMemberPhone() {
        return memberPhone;
    }

    public void setMemberPhone(String memberPhone) {
        this.memberPhone = memberPhone;
    }

    public String getFeedbackContent() {
        return feedbackContent;
    }

    public void setFeedbackContent(String feedbackContent) {
        this.feedbackContent = feedbackContent;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }
}
