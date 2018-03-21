package finley.gmair.model.wechat;

import finley.gmair.model.Entity;

public class AutoReply extends Entity {
    private String replyId;

    private String messageType;

    private String keyWord;

    private String templateId;

    public AutoReply() {
        super();
    }

    public AutoReply(String replyId, String messageType, String keyWord, String templateId) {
        this();
        this.replyId = replyId;
        this.messageType = messageType;
        this.keyWord = keyWord;
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

    public String getKeyWord() {
        return keyWord;
    }

    public void setKeyWord(String keyWord) {
        this.keyWord = keyWord;
    }

    public String getTemplateId() {
        return templateId;
    }

    public void setTemplateId(String templateId) {
        this.templateId = templateId;
    }
}