package finley.gmair.model.wechat;

import finley.gmair.model.Entity;

public class TextTemplate extends Entity {
    private String templateId;

    private String messageType;

    private String response;

    public TextTemplate() {
        super();
    }

    public TextTemplate(String messageType, String response) {
        this();
        this.messageType = messageType;
        this.response = response;
    }

    public String getTemplateId() {
        return templateId;
    }

    public void setTemplateId(String templateId) {
        this.templateId = templateId;
    }

    public String getMessageType() {
        return messageType;
    }

    public void setMessageType(String messageType) {
        this.messageType = messageType;
    }

    public String getResponse() {
        return response;
    }

    public void setResponse(String response) {
        this.response = response;
    }
}