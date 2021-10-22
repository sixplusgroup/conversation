package finley.gmair.model.wechat;

import finley.gmair.model.Entity;

public class MessageTemplate extends Entity {

    private String templateId;

    private String messageType;

    private String messageContent;

    public MessageTemplate(){
        super();
    }

    public MessageTemplate(String templateId,String messageType,String messageContent){
        this();
        this.messageContent = messageContent;
        this.messageType = messageType;
        this.templateId = templateId;
    }

    public void setMessageType(String messageType) {
        this.messageType = messageType;
    }

    public String getMessageType() {
        return messageType;
    }

    public void setTemplateId(String templateId) {
        this.templateId = templateId;
    }

    public String getTemplateId() {
        return templateId;
    }

    public void setMessageContent(String messageContent) {
        this.messageContent = messageContent;
    }

    public String getMessageContent() {
        return messageContent;
    }
}
