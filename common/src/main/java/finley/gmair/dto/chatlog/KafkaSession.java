package finley.gmair.dto.chatlog;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class KafkaSession implements Serializable {
    int sessionId;
    List<KafkaMessage> messages;
}
