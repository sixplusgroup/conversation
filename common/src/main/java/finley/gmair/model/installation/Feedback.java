package finley.gmair.model.installation;

import finley.gmair.model.Entity;

public class Feedback extends Entity {
    private String feedbackId;
    private String qrcode;
    private String memberPhone;
    private String feedbackContent;
    public Feedback()
    {
        super();
    }

    public Feedback(String qrcode, String memberPhone, String feedbackContent) {
        this();
        this.qrcode = qrcode;
        this.memberPhone = memberPhone;
        this.feedbackContent = feedbackContent;
    }

    public String getFeedbackId() {
        return feedbackId;
    }

    public void setFeedbackId(String feedbackId) {
        this.feedbackId = feedbackId;
    }

    public String getQrcode() {
        return qrcode;
    }

    public void setQrcode(String qrcode) {
        this.qrcode = qrcode;
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
}
