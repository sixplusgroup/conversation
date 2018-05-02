package finley.gmair.form.wechat;

import finley.gmair.model.wechat.DescriptionType;

public class ArticleTemplateForm {
    private String inMessageType;

    private String articleTitle;

    private DescriptionType descriptionType;

    private String descriptionContent;

    private String keyword;

    private String pictureUrl;

    private String articleUrl;

    public ArticleTemplateForm() {
        super();
    }

    public String getInMessageType() {
        return inMessageType;
    }

    public void setInMessageType(String inMessageType) {
        this.inMessageType = inMessageType;
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

    public String getKeyword() {
        return keyword;
    }

    public void setKeyword(String keyword) {
        this.keyword = keyword;
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