package finley.gmair.form.wechat;

public class PictureTemplateForm {
    private String inMessageType;

    private String keyword;

    private String pictureUrl;

    public PictureTemplateForm() {
        super();
    }

    public String getInMessageType() {
        return inMessageType;
    }

    public void setInMessageType(String inMessageType) {
        this.inMessageType = inMessageType;
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
}