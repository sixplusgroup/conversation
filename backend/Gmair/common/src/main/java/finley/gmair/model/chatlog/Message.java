package finley.gmair.model.chatlog;

import finley.gmair.enums.chatlog.SentimentLabel;
import lombok.Data;
import lombok.experimental.Accessors;

@Data
@Accessors(chain = true)
public class Message {
    Integer id;
    int sessionId;
    String content;
    boolean isFromWaiter;
    long timestamp;
    SentimentLabel label;
    double score;

}
