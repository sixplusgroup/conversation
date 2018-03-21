package finley.gmair.model.wechat;

import finley.gmair.model.Entity;

public class PictureTemplate extends Entity {
    private String templateId;

    private String pictureUrl;

    public PictureTemplate() {
        super();
    }

    private PictureTemplate(String templateId, String pictureUrl) {
        this();
        this.templateId = templateId;
        this.pictureUrl = pictureUrl;
    }

    public String getTemplateId() {
        return templateId;
    }

    public void setTemplateId(String templateId) {
        this.templateId = templateId;
    }

    public String getPictureUrl() {
        return pictureUrl;
    }

    public void setPictureUrl(String pictureUrl) {
        this.pictureUrl = pictureUrl;
    }
}