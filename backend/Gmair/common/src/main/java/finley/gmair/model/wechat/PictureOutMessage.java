package finley.gmair.model.wechat;

public class PictureOutMessage {
    private String ToUserName;
    private String FromUserName;
    private long CreateTime;
    private String MsgType = "image";
    private Image Image;

    public String getToUserName() {
        return ToUserName;
    }

    public void setToUserName(String toUserName) {
        ToUserName = toUserName;
    }

    public String getFromUserName() {
        return FromUserName;
    }

    public void setFromUserName(String fromUserName) {
        FromUserName = fromUserName;
    }

    public long getCreateTime() {
        return CreateTime;
    }

    public void setCreateTime(long createTime) {
        CreateTime = createTime;
    }

    public String getMsgType() {
        return MsgType;
    }

    public void setMsgType(String msgType) {
        MsgType = msgType;
    }

    public finley.gmair.model.wechat.Image getImage() {
        return Image;
    }

    public void setImage(finley.gmair.model.wechat.Image image) {
        Image = image;
    }
}