package finley.gmair.dto.chatlog;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.Accessors;

import java.io.Serializable;

@Data
@NoArgsConstructor
@Accessors(chain = true)
public class KafkaMessage implements Serializable {
    int messageId;
    String content;
    double score;
    int label;
}
