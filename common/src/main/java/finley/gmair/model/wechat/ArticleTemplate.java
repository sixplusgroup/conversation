package finley.gmair.model.wechat;

import finley.gmair.model.Entity;

public class ArticleTemplate extends Entity {
    private String templateId;

    private String articleTitle;

    private DescriptionType descriptionType;

    private String descriptionContent;

    private String pictureUrl;

    private String articleUrl;

    public ArticleTemplate() {
        super();
        this.descriptionType = DescriptionType.TEXT;
    }

    public ArticleTemplate(String templateId, String articleTitle, DescriptionType descriptionType, String descriptionContent, String pictureUrl, String articleUrl) {
        this();
        this.templateId = templateId;
        this.articleTitle = articleTitle;
        this.descriptionType = descriptionType;
        this.descriptionContent = descriptionContent;
        this.pictureUrl = pictureUrl;
        this.articleUrl = articleUrl;
    }

    public String getTemplateId() {
        return templateId;
    }

    public void setTemplateId(String templateId) {
        this.templateId = templateId;
    }

    public String getArticleTitle() {
        return articleTitle;
    }

    public void setArticleTitle(String articleTitle) {
        this.articleTitle = articleTitle;
    }

    public DescriptionType getDescriptionType() {
        return descriptionType;
    }

    public void setDescriptionType(DescriptionType descriptionType) {
        this.descriptionType = descriptionType;
    }

    public String getDescriptionContent() {
        return descriptionContent;
    }

    public void setDescriptionContent(String descriptionContent) {
        this.descriptionContent = descriptionContent;
    }

    public String getPictureUrl() {
        return pictureUrl;
    }

    public void setPictureUrl(String pictureUrl) {
        this.pictureUrl = pictureUrl;
    }

    public String getArticleUrl() {
        return articleUrl;
    }

    public void setArticleUrl(String articleUrl) {
        this.articleUrl = articleUrl;
    }
}