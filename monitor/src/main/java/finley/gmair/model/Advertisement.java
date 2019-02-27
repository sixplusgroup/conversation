package finley.gmair.model;

/**
 * @author fan
 * @create_time 2019-2019/2/27 10:25 AM
 */
public class Advertisement extends Entity {
    private String adsId;

    private String content;

    public Advertisement() {
        super();
    }

    public String getAdsId() {
        return adsId;
    }

    public void setAdsId(String adsId) {
        this.adsId = adsId;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }
}
