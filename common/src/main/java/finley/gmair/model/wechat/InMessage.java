package finley.gmair.model.wechat;

public class InMessage {
    private String ToUserName;
    private String FromUserName;
    private String MsgType;
    private long CreateTime;

    public InMessage() {
        super();
    }

    public InMessage(String toUserName, String fromUserName, String msgType, long createTime) {
        ToUserName = toUserName;
        FromUserName = fromUserName;
        MsgType = msgType;
        CreateTime = createTime;
    }

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

    public String getMsgType() {
        return MsgType;
    }

    public void setMsgType(String msgType) {
        MsgType = msgType;
    }

    public long getCreateTime() {
        return CreateTime;
    }

    public void setCreateTime(long createTime) {
        CreateTime = createTime;
    }
}