package finley.gmair.model.message;

import finley.gmair.model.Entity;

public class MessageTemplate extends Entity {
    private String templateId;

    private String message;

    public MessageTemplate() {
        super();
    }

    public MessageTemplate(String message) {
        this();
        this.message = message;
    }

    public String getTemplateId() {
        return templateId;
    }

    public void setTemplateId(String templateId) {
        this.templateId = templateId;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }
}
