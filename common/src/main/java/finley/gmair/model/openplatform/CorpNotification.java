package finley.gmair.model.openplatform;

import finley.gmair.model.Entity;

/**
 * @ClassName: CorpNotification
 * @Description: TODO
 * @Author fan
 * @Date 2019/11/13 1:57 PM
 */
public class CorpNotification extends Entity {
    private String notificationId;

    private String corpId;

    private String url;

    private String param;

    private String header;

    public CorpNotification() {
        super();
    }

    public CorpNotification(String corpId, String url) {
        this.corpId = corpId;
        this.url = url;
    }

    public String getNotificationId() {
        return notificationId;
    }

    public void setNotificationId(String notificationId) {
        this.notificationId = notificationId;
    }

    public String getCorpId() {
        return corpId;
    }

    public void setCorpId(String corpId) {
        this.corpId = corpId;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public String getParam() {
        return param;
    }

    public void setParam(String param) {
        this.param = param;
    }

    public String getHeader() {
        return header;
    }

    public void setHeader(String header) {
        this.header = header;
    }
}
