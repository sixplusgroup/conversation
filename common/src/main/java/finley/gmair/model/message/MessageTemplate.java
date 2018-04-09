package finley.gmair.model.message;

import finley.gmair.model.Entity;

public class MessageTemplate extends Entity {
    private String templateId;

    private MessageCatalog catalog;

    private String message;

    public MessageTemplate() {
        super();
    }

    public MessageTemplate(MessageCatalog catalog, String message) {
        this();
        this.catalog = catalog;
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

    public MessageCatalog getCatalog() {
        return catalog;
    }

    public void setCatalog(MessageCatalog catalog) {
        this.catalog = catalog;
    }
}
