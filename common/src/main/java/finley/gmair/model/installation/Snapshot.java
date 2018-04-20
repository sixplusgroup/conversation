package finley.gmair.model.installation;

import finley.gmair.model.Entity;

public class Snapshot extends Entity {

    private String snapshotId;
    private String assignId;
    private String qrcode;
    private String wechatId;
    private String memberPhone;
    private String checkList;
    private String indoorHole;
    private String outdoorHole;
    private String indoorPreAir;
    private String indoorPostAir;
    private String holeDirection;

    public Snapshot()
    {
        super();
    }

    public Snapshot(String assignId, String qrcode, String wechatId, String memberPhone, String checkList, String indoorHole, String outdoorHole, String indoorPreAir, String indoorPostAir, String holeDirection) {
        this();
        this.assignId = assignId;
        this.qrcode = qrcode;
        this.wechatId = wechatId;
        this.memberPhone = memberPhone;
        this.checkList = checkList;
        this.indoorHole = indoorHole;
        this.outdoorHole = outdoorHole;
        this.indoorPreAir = indoorPreAir;
        this.indoorPostAir = indoorPostAir;
        this.holeDirection = holeDirection;
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

    public String getCheckList() {
        return checkList;
    }

    public void setCheckList(String checkList) {
        this.checkList = checkList;
    }

    public String getIndoorHole() {
        return indoorHole;
    }

    public void setIndoorHole(String indoorHole) {
        this.indoorHole = indoorHole;
    }

    public String getOutdoorHole() {
        return outdoorHole;
    }

    public void setOutdoorHole(String outdoorHole) {
        this.outdoorHole = outdoorHole;
    }

    public String getIndoorPreAir() {
        return indoorPreAir;
    }

    public void setIndoorPreAir(String indoorPreAir) {
        this.indoorPreAir = indoorPreAir;
    }

    public String getIndoorPostAir() {
        return indoorPostAir;
    }

    public void setIndoorPostAir(String indoorPostAir) {
        this.indoorPostAir = indoorPostAir;
    }

    public String getHoleDirection() {
        return holeDirection;
    }

    public void setHoleDirection(String holeDirection) {
        this.holeDirection = holeDirection;
    }


}
