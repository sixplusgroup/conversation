package finley.gmair.model.chatlog;

import lombok.Data;
import lombok.experimental.Accessors;

@Data
@Accessors(chain = true)
public class Message {
    int messageId;
    int sessionId;
    String content;
    boolean isFromWaiter;
    String analysis;
    long timestamp;
    int label;
    double score;

}
