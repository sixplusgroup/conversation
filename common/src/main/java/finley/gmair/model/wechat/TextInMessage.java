package finley.gmair.model.wechat;

public class TextInMessage extends AbstractInMessage {
    private String Content;
    private long MsgId;

    public TextInMessage() {
        super();
    }

    public TextInMessage(String content, long msgId) {
        this();
        this.Content = content;
        this.MsgId = msgId;
    }

    public String getContent() {
        return Content;
    }

    public void setContent(String content) {
        Content = content;
    }

    public long getMsgId() {
        return MsgId;
    }

    public void setMsgId(long msgId) {
        MsgId = msgId;
    }
}