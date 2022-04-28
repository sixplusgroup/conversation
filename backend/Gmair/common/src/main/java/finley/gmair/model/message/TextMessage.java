package finley.gmair.model.message;

import finley.gmair.model.Entity;

public class TextMessage extends Entity {
    private String messageId;

    private String phone;

    private String text;

    public TextMessage() {
        super();
    }

    public TextMessage(String phone, String text) {
        this();
        this.phone = phone;
        this.text = text;
    }

    public String getMessageId() {
        return messageId;
    }

    public void setMessageId(String messageId) {
        this.messageId = messageId;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }
}
