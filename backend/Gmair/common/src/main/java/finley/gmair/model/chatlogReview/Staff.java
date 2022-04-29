package finley.gmair.model.chatlogReview;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class Staff {
    private int waiterId;
    private String waiterName;
}
