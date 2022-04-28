package finley.gmair.model.wechat;

import finley.gmair.model.Entity;

/**
 * @ClassName: ReplyRecord
 * @Description: TODO
 * @Author fan
 * @Date 2021/1/8 4:27 PM
 */
public class ReplyRecord extends Entity {
    private String recordId;

    private String userId;

    private String templateId;

    private String input;

    public ReplyRecord() {
        super();
    }

    public ReplyRecord(String userId, String templateId, String input) {
        this();
        this.userId = userId;
        this.templateId = templateId;
        this.input = input;
    }

    public String getRecordId() {
        return recordId;
    }

    public void setRecordId(String recordId) {
        this.recordId = recordId;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public String getTemplateId() {
        return templateId;
    }

    public void setTemplateId(String templateId) {
        this.templateId = templateId;
    }

    public String getInput() {
        return input;
    }

    public void setInput(String input) {
        this.input = input;
    }
}
