package finley.gmair.model.chatlogReview;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class Session {
    private int sessionId;
    private String originalSessionId;
    private int userId;
    private int waiterId;
    private String productId;
    private Long timestamp;
    private double customerAverageScore;
    private int customerExtremeNegativeCount;
    private int waiterNegativeCount;
    private int messageNum;
    private boolean isTypical;
}
