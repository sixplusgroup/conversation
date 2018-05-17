package finley.gmair.model.installation;

import finley.gmair.model.Entity;

public class Snapshot extends Entity {

    private String snapshotId;
    private String assignId;
    private String qrcode;
    private String wechatId;
    private String memberPhone;
    private String picPath;
    private boolean net;

    public Snapshot() {
        super();
    }

    public Snapshot(String assignId, String qrcode, String wechatId, String memberPhone, String picPath, boolean net) {
        this();
        this.assignId = assignId;
        this.qrcode = qrcode;
        this.wechatId = wechatId;
        this.memberPhone = memberPhone;
        this.picPath = picPath;
        this.net = net;
    }

    public String getSnapshotId() {
        return snapshotId;
    }

    public void setSnapshotId(String snapshotId) {
        this.snapshotId = snapshotId;
    }

    public String getAssignId() {
        return assignId;
    }

    public void setAssignId(String assignId) {
        this.assignId = assignId;
    }

    public String getQrcode() {
        return qrcode;
    }

    public void setQrcode(String qrcode) {
        this.qrcode = qrcode;
    }

    public String getWechatId() {
        return wechatId;
    }

    public void setWechatId(String wechatId) {
        this.wechatId = wechatId;
    }

    public String getMemberPhone() {
        return memberPhone;
    }

    public void setMemberPhone(String memberPhone) {
        this.memberPhone = memberPhone;
    }

    public String getPicPath() {
        return picPath;
    }

    public void setPicPath(String picPath) {
        this.picPath = picPath;
    }

    public boolean isNet() {
        return net;
    }

    public void setNet(boolean net) {
        this.net = net;
    }
}
