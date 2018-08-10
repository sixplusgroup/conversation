package finley.gmair.model.wechat;

import com.thoughtworks.xstream.annotations.XStreamAlias;

public class Image {
    @XStreamAlias("MediaId")
    private String MediaId;

    public String getMediaId() {
        return MediaId;
    }

    public void setMediaId(String mediaId) {
        MediaId = mediaId;
    }

    public Image(String mediaId) {
        MediaId = mediaId;
    }
}
