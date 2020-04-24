package finley.gmair.form.wechat;

public class VideoTemplateForm {
    private String inMessageType;

    private String keyword;

    private String videoUrl;

    private String videoTitle;

    private String videoDesc;

    public VideoTemplateForm() {
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

    public String getVideoUrl() {
        return videoUrl;
    }

    public void setVideoUrl(String videoUrl) {
        this.videoUrl = videoUrl;
    }

    public String getVideoTitle() {
        return videoTitle;
    }

    public void setVideoTitle(String videoTitle) {
        this.videoTitle = videoTitle;
    }

    public String getVideoDesc() {
        return videoDesc;
    }

    public void setVideoDesc(String videoDesc) {
        this.videoDesc = videoDesc;
    }
}