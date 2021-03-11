package finley.gmair.model.minivoice;

import finley.gmair.model.Entity;

/**
 * 用户和系统之间的对话
 */
public class Dialog extends Entity {

    String openid;

    String content;

    boolean isSystem;

    public Dialog() {
    }

    public Dialog(String openid, String content, boolean isSystem) {
        this.openid = openid;
        this.content = content;
        this.isSystem = isSystem;
    }

    public String getOpenid() {
        return openid;
    }

    public void setOpenid(String openid) {
        this.openid = openid;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public boolean isSystem() {
        return isSystem;
    }

    public void setSystem(boolean system) {
        isSystem = system;
    }

}
