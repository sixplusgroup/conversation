package finley.gmair.model.wechat;

import finley.gmair.model.Entity;

public class VideoTemplate extends Entity {
    private String templateId;

    private String videoUrl;

    private String videoTitle;

    private String videoDesc;

    public VideoTemplate() {
        super();
    }

    public VideoTemplate(String videoUrl, String videoTitle, String videoDesc) {
        this();
        this.videoUrl = videoUrl;
        this.videoTitle = videoTitle;
        this.videoDesc = videoDesc;
    }

    public String getTemplateId() {
        return templateId;
    }

    public void setTemplateId(String templateId) {
        this.templateId = templateId;
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