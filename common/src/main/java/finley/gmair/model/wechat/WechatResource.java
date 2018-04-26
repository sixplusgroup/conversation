package finley.gmair.model.wechat;

import finley.gmair.model.Entity;

public class WechatResource extends Entity{
    private String resourceId;

    private String resourceName;

    public WechatResource() {
        super();
    }

    public WechatResource(String resourceId, String resourceName) {
        this();
        this.resourceId = resourceId;
        this.resourceName = resourceName;
    }

    public String getResourceId() {
        return resourceId;
    }

    public void setResourceId(String resourceId) {
        this.resourceId = resourceId;
    }

    public String getResourceName() {
        return resourceName;
    }

    public void setResourceName(String resourceName) {
        this.resourceName = resourceName;
    }
}