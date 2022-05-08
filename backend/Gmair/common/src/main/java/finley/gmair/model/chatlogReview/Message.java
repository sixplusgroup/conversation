package finley.gmair.model.chatlogReview;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class Message {
    private int messageId;
    private int sessionId;
    private String content;
    private boolean isFromWaiter;
    private Long timestamp;
    private String label;
    private double score;
}
