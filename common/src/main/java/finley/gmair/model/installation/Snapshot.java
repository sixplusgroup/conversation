package finley.gmair.model.installation;

import finley.gmair.model.Entity;

public class Snapshot extends Entity {

    private String snapshotId;
    private String assignId;
    private String codeValue;
    private String wechatId;
    private String memberPhone;
    private String picPath;
    private boolean net;
    private String installType;

    public Snapshot() {
        super();
    }

    public Snapshot(String assignId, String qrcode, String wechatId, String memberPhone, String picPath, boolean net,String installType) {
        this();
        this.assignId = assignId;
        this.codeValue = qrcode;
        this.wechatId = wechatId;
        this.memberPhone = memberPhone;
        this.picPath = picPath;
        this.net = net;
        this.installType = installType;
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

    public String getCodeValue() {
        return codeValue;
    }

    public void setCodeValue(String codeValue) {
        this.codeValue = codeValue;
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

    public String getInstallType() {
        return installType;
    }

    public void setInstallType(String installType) {
        this.installType = installType;
    }
}
