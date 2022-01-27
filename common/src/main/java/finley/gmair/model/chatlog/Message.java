package finley.gmair.model.chatlog;

import lombok.Data;

@Data
public class Message {
    int messageId;
    int sessionId;
    String content;
    boolean isFromWaiter;
    String analysis;
    long timestamp;
}
