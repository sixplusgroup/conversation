package finley.gmair.model.wechat;

import finley.gmair.model.Entity;

public class AutoReply extends Entity {
    private String replyId;

    private String messageType;

    private String keyword;

    private String templateId;

    public AutoReply() {
        super();
    }

    public AutoReply(String messageType, String keyword, String templateId) {
        this();
        this.messageType = messageType;
        this.keyword = keyword;
        this.templateId = templateId;
    }

    public String getReplyId() {
        return replyId;
    }

    public void setReplyId(String replyId) {
        this.replyId = replyId;
    }

    public String getMessageType() {
        return messageType;
    }

    public void setMessageType(String messageType) {
        this.messageType = messageType;
    }

    public String getKeyword() {
        return keyword;
    }

    public void setKeyword(String keyword) {
        this.keyword = keyword;
    }

    public String getTemplateId() {
        return templateId;
    }

    public void setTemplateId(String templateId) {
        this.templateId = templateId;
    }
}