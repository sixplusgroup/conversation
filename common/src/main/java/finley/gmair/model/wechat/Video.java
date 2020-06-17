package finley.gmair.model.wechat;

import com.thoughtworks.xstream.annotations.XStreamAlias;

public class Video {
    @XStreamAlias("MediaId")
    private String MediaId;

    @XStreamAlias("Title")
    private String Title;

    @XStreamAlias("Description")
    private String Description;

    public String getMediaId() {
        return MediaId;
    }

    public void setMediaId(String mediaId) {
        MediaId = mediaId;
    }

    public String getTitle() {
        return Title;
    }

    public void setTitle(String title) {
        Title = title;
    }

    public String getDescription() {
        return Description;
    }

    public void setDescription(String description) {
        Description = description;
    }

    public Video(String mediaId, String title, String description) {
        MediaId = mediaId;
        Title = title;
        Description = description;
    }
}
