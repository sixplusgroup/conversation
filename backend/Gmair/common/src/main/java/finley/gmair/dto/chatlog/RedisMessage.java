package finley.gmair.dto.chatlog;

import finley.gmair.enums.chatlog.SentimentLabel;
import lombok.Data;
import lombok.experimental.Accessors;

@Data
@Accessors(chain = true)
public class RedisMessage {
    SentimentLabel label;
    double score;
}
